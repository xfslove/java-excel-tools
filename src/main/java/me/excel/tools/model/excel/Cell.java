package me.excel.tools.model.excel;

import java.io.Serializable;

/**
 * excel cell
 * <p>
 * Created by hanwen on 15-12-16.
 */
public interface Cell extends Serializable {

  String getField();

  void setField(String field);

  /**
   * cell row index
   *
   * @return 1-based
   */
  int getRowIndex();

  /**
   * cell column index
   *
   * @return 1-based
   */
  int getColumnIndex();

  /**
   * cell value
   *
   * @return value
   */
  String getValue();

  /**
   * @return sheet of cell at
   */
  Sheet getSheet();

  /**
   * @return row of cell at
   */
  Row getRow();
}