package spreadsheet.mapper.w2o.validation.engine;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spreadsheet.mapper.model.core.Cell;
import spreadsheet.mapper.model.core.Row;
import spreadsheet.mapper.model.meta.FieldMeta;
import spreadsheet.mapper.model.meta.SheetMeta;
import spreadsheet.mapper.model.msg.Message;
import spreadsheet.mapper.model.msg.MessageBean;
import spreadsheet.mapper.model.msg.MessageWriteStrategies;
import spreadsheet.mapper.w2o.validation.WorkbookValidateException;
import spreadsheet.mapper.w2o.validation.validator.cell.DependencyValidator;
import spreadsheet.mapper.w2o.validation.validator.cell.MultiCellValidator;
import spreadsheet.mapper.w2o.validation.validator.cell.SingleCellValidator;

import java.util.*;

/**
 * use simply dfs to do dependency validate, depends on dependencies no cycle {@link DependencyCycleCheckEngine}
 * <p>
 * Created by hanwen on 2017/1/6.
 */
public class DependencyValidateEngine {

  private static final Logger LOGGER = LoggerFactory.getLogger(DependencyValidateEngine.class);

  private LinkedHashMap<String, List<DependencyValidator>> validatorMap = new LinkedHashMap<>();

  private LinkedHashMap<String, LinkedHashSet<String>> vGraph = new LinkedHashMap<>();
  private Map<String, Boolean> visited = new HashMap<>();

  // each node validate result
  private Map<String, Boolean> result = new HashMap<>();

  private List<Message> errorMessages = new ArrayList<>();

  public DependencyValidateEngine(LinkedHashMap<String, List<DependencyValidator>> validatorMap) {
    this.vGraph = DependencyEngineHelper.buildVGraph(validatorMap);
    for (String v : vGraph.keySet()) {
      visited.put(v, false);
      result.put(v, true);
    }
    this.validatorMap = validatorMap;
  }

  public boolean valid(Row row, SheetMeta sheetMeta) {

    for (String v : vGraph.keySet()) {
      if (!visited.get(v)) {
        dfs(row, sheetMeta, v);
      }
    }

    return !result.values().contains(Boolean.FALSE);
  }

  public List<Message> getErrorMessages() {
    return errorMessages;
  }

  private void dfs(Row row, SheetMeta sheetMeta, String v) {

    for (String w : vGraph.get(v)) {
      if (!visited.get(w)) {
        dfs(row, sheetMeta, w);
      }
    }

    visited.put(v, true);

    if (ifSkip(v)) {
      result.put(v, false);
      return;
    }

    List<DependencyValidator> validators = validatorMap.get(v);

    LOGGER.debug("do valid at group:[" + v + "] and validator numbers of this group is:[" + validators.size() + "]");

    for (DependencyValidator validator : validators) {
      // if one of the group valid failure skip rest validators
      if (!doValid(row, sheetMeta, validator)) {
        LOGGER.debug("valid false at group:[" + v + "]");
        result.put(v, false);
        break;
      }
    }
  }

  private boolean ifSkip(String v) {
    for (String w : vGraph.get(v)) {
      if (!result.get(w)) {
        LOGGER.debug("skip valid at group:[" + v + "] because of depends on group:[" + w + "] validate failure");
        return true;
      }
    }
    return false;
  }

  private boolean doValid(Row row, SheetMeta sheetMeta, DependencyValidator dependencyValidator) {
    if (dependencyValidator instanceof MultiCellValidator) {

      MultiCellValidator multiCellValidator = (MultiCellValidator) dependencyValidator;

      LinkedHashSet<String> matchFields = multiCellValidator.getMatchFields();
      if (CollectionUtils.isEmpty(matchFields)) {
        throw new WorkbookValidateException("cell validator[" + multiCellValidator.getClass().getName() + "] match fields can not be null");
      }

      List<Cell> validCells = new ArrayList<>();
      List<FieldMeta> fieldMetas = new ArrayList<>();
      for (String matchField : matchFields) {

        FieldMeta fieldMeta = sheetMeta.getFieldMeta(matchField);

        if (fieldMeta == null) {
          LOGGER.debug("no field meta named:[" + matchField + "], cell validator[" + multiCellValidator.getClass().getName() + "] ignored");
          return true;
        }

        fieldMetas.add(fieldMeta);
        validCells.add(row.getCell(fieldMeta.getColumnIndex()));
      }

      boolean result = multiCellValidator.valid(validCells, fieldMetas);

      if (!result) {

        String errorMessage = multiCellValidator.getErrorMessage();

        if (StringUtils.isNotBlank(errorMessage)) {

          for (FieldMeta fieldMeta : fieldMetas) {
            errorMessages.add(new MessageBean(MessageWriteStrategies.COMMENT, errorMessage, row.getSheet().getIndex(), row.getIndex(), fieldMeta.getColumnIndex()));
          }
        }

      }

      return result;
    } else {

      SingleCellValidator singleCellValidator = (SingleCellValidator) dependencyValidator;
      String matchField = singleCellValidator.getMatchField();
      if (StringUtils.isBlank(matchField)) {
        throw new WorkbookValidateException("cell validator[" + singleCellValidator.getClass().getName() + "] match field can not be null");
      }

      FieldMeta fieldMeta = sheetMeta.getFieldMeta(matchField);

      if (fieldMeta == null) {
        LOGGER.debug("no field meta named:[" + matchField + "], cell validator[" + singleCellValidator.getClass().getName() + "] ignored");
        return true;
      }

      Cell cell = row.getCell(fieldMeta.getColumnIndex());
      boolean result = singleCellValidator.valid(cell, fieldMeta);

      if (!result) {

        String errorMessage = singleCellValidator.getErrorMessage();

        if (StringUtils.isNotBlank(errorMessage)) {
          errorMessages.add(new MessageBean(MessageWriteStrategies.COMMENT, errorMessage, row.getSheet().getIndex(), row.getIndex(), fieldMeta.getColumnIndex()));
        }

      }

      return result;
    }
  }
}
