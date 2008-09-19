/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rednoblue.jRecipe.logging;

/**
 *
 * @author John
 */
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

//This custom formatter formats parts of a log record to a single line
class MyFormatter extends Formatter {
  // This method is called for every log record
  public String format(LogRecord rec) {
    StringBuffer buf = new StringBuffer(1000);
    // Bold any levels >= WARNING
    buf.append(calcDate(rec.getMillis()));
    buf.append('\t');
    buf.append(rec.getSourceMethodName());
    buf.append('\t');
    buf.append(rec.getLevel());
    buf.append('\t');
    buf.append(formatMessage(rec));
    buf.append('\n');
    return buf.toString();
  }

  private String calcDate(long millisecs) {
    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date resultdate = new Date(millisecs);
    return date_format.format(resultdate);
  }

  // This method is called just after the handler using this
  // formatter is created
  public String getHead(Handler h) {
    return "";
  }

  // This method is called just after the handler using this
  // formatter is closed
  public String getTail(Handler h) {
    return "";
  }
}