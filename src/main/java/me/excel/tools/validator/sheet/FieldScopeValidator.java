package me.excel.tools.validator.sheet;

import me.excel.tools.model.excel.Cell;
import me.excel.tools.model.excel.Row;
import me.excel.tools.model.excel.Sheet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 * field scope validator,
 *
 * it useful when you only want afterSheet some special fields of a object, this validator can prevent unexpected things, for security.
 *
 * eg : class A has fields [A, other fields...].
 * if you only want modify A, you can supplied the {@link FieldScopeValidator#fieldScopes} as [A], when the excel files fields has others,
 * this validator will get false.
 * </pre>
 * Created by hanwen on 4/26/16.
 */
public class FieldScopeValidator implements SheetValidator {

  private List<String> fieldScopes = new ArrayList<>();

  private String errorMessage;

  public FieldScopeValidator(String... fieldScopes) {
    Collections.addAll(this.fieldScopes, fieldScopes);
    this.errorMessage = "有字段不在处理范围内";
  }

  public FieldScopeValidator(String errorMessage, String... fieldScopes) {
    Collections.addAll(this.fieldScopes, fieldScopes);
    this.errorMessage = errorMessage;
  }

  @Override
  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  public boolean validate(Sheet sheet) {
    Row fieldRow = sheet.getFieldRow();

    for (Cell fieldCell : fieldRow.getCells()) {
      String field = fieldCell.getValue();
      if (!fieldScopes.contains(field)) {
        return false;
      }
    }

    return true;
  }

}