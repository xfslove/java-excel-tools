package spreadsheet.mapper.w2o.validation;

import spreadsheet.mapper.model.core.Sheet;
import spreadsheet.mapper.model.core.Workbook;
import spreadsheet.mapper.model.meta.SheetMeta;
import spreadsheet.mapper.model.meta.WorkbookMeta;
import spreadsheet.mapper.model.msg.Message;
import spreadsheet.mapper.w2o.validation.validator.workbook.WorkbookValidator;

import java.util.List;

/**
 * workbook validation helper
 * <p>
 * Created by hanwen on 2017/1/4.
 */
public interface WorkbookValidationHelper {

  /**
   * @param workbookValidator {@link WorkbookValidator}
   * @return {@link WorkbookValidationHelper}
   */
  WorkbookValidationHelper addWorkbookValidator(WorkbookValidator workbookValidator);

  /**
   * the sequence of the sheet validation helper add is the helper used to valid workbook's sheets sequence.
   *
   * @param sheetValidationHelper {@link SheetValidationHelper}
   * @return {@link WorkbookValidationHelper}
   */
  WorkbookValidationHelper addSheetValidationHelper(SheetValidationHelper sheetValidationHelper);

  /**
   * @param workbook     {@link Workbook}
   * @param workbookMeta {@link WorkbookMeta}
   * @return true if pass all
   * @see SheetValidationHelper#valid(Sheet, SheetMeta)
   */
  boolean valid(Workbook workbook, WorkbookMeta workbookMeta);

  /**
   * @return error messages
   * @see SheetValidationHelper#getErrorMessages()
   */
  List<Message> getErrorMessages();
}
