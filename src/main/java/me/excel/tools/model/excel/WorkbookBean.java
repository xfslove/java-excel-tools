package me.excel.tools.model.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanwen on 15-12-16.
 */
public class WorkbookBean implements Workbook {

  private boolean after97 = true;

  private final List<Sheet> sheets = new ArrayList<>();

  public WorkbookBean() {
    // default constructor
  }

  public boolean isAfter97() {
    return after97;
  }

  public void setAfter97(boolean after97) {
    this.after97 = after97;
  }

  @Override
  public List<Sheet> getSheets() {
    return sheets;
  }

  @Override
  public int sizeOfSheets() {
    return sheets.size();
  }

  @Override
  public boolean addSheet(Sheet sheet) {
    boolean success = sheets.add(sheet);
    ((SheetBean) sheet).setIndex(sizeOfSheets() + 1);
    ((SheetBean) sheet).setWorkbook(this);
    return success;
  }

  @Override
  public Sheet getSheet(int index) {
    if (index < 1) {
      throw new IllegalArgumentException("index must greater than zero");
    }
    return sheets.get(index - 1);
  }

  @Override
  public Sheet getLastSheet() {
    if (sizeOfSheets() == 0) {
      return null;
    }
    return getSheet(sizeOfSheets());
  }

  @Override
  public Sheet getFirstSheet() {
    if (sizeOfSheets() == 0) {
      return null;
    }
    return getSheet(1);
  }
}