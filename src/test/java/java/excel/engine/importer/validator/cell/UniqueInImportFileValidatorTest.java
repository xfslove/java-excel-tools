package java.excel.engine.importer.validator.cell;

import java.excel.engine.model.excel.CellBean;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Created by hanwen on 2016/12/22.
 */
public class UniqueInImportFileValidatorTest {

  @Test
  public void testCustomValidate() throws Exception {

    UniqueInImportFileValidator uniqueInImportFileValidator = new UniqueInImportFileValidator("person.unique");

    CellBean e1 = new CellBean(1, 1, "1");
    CellBean e2 = new CellBean(2, 1, "2");
    CellBean e3 = new CellBean(3, 1, "3");
    CellBean e4 = new CellBean(4, 1, "4");
    CellBean e5 = new CellBean(5, 1, "5");
    CellBean e6 = new CellBean(6, 1, "1");
    e1.setField("person.unique");
    e2.setField("person.unique");
    e3.setField("person.unique");
    e4.setField("person.unique");
    e5.setField("person.unique");
    e6.setField("person.unique");

    assertTrue(uniqueInImportFileValidator.valid(e1));
    assertTrue(uniqueInImportFileValidator.valid(e2));
    assertTrue(uniqueInImportFileValidator.valid(e3));
    assertTrue(uniqueInImportFileValidator.valid(e4));
    assertTrue(uniqueInImportFileValidator.valid(e5));
    assertFalse(uniqueInImportFileValidator.valid(e6));

  }

}