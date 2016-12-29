package java.excel.engine.exporter.extractor;

import java.excel.engine.exception.ExcelProcessException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.NestedNullException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.excel.engine.util.FieldUtils;


/**
 * default value extractor, using {@link BeanUtils#getProperty(Object, String)}
 * <p>
 * Created by hanwen on 15-12-16.
 */
public class DefaultValueExtractor {

  private static final Logger LOGGER = LoggerFactory.getLogger(DefaultValueExtractor.class);

  public String getStringValue(Object data, String field) {

    try {
      return BeanUtils.getProperty(data, FieldUtils.detectRealField(field));
    } catch (NestedNullException e) {
      LOGGER.trace(ExceptionUtils.getStackTrace(e));
      return null;
    } catch (Exception e) {
      LOGGER.error(ExceptionUtils.getStackTrace(e));
      throw new ExcelProcessException(e);
    }
  }

}