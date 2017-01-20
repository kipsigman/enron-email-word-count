package kipsigman.enron

/**
 * Utility object to parse an email into distinct headers and body.
 */
object EmailParser {

  object Key {
    val Body = "Body"
    val Cc = "Cc"
    val From = "From"
    val Subject = "Subject"
  }

  private val endOfHeaderMarker = ""
  private val emptyKeyRegex = """(.*?):[\s]*""".r
  private val keyValueRegex = """(.*?): (.*)""".r

  def parse(emailStr: String): Map[String, String] = {
    val emailLines = emailStr.split("\n")
    parse(emailLines)
  }

  
  /**
   * Returns a map containing parsed email headers and body.
   * For example, the line "Subject: Test" would create
   * a key "Subject" and a value of "Test".
   *
   * Some headers may take up multiple lines. We create one line key/value pairs
   * by replacing the newline with a space. 
   * 
   * A blank line denotes the separator between the header section and body.
   * Everything after this separator is stored in the Body key, newlines are kept intact here.
   */
  def parse(emailLines: Seq[String]): Map[String, String] = {

    def parseAccumulate(unprocessedLines: Seq[String], emailMap: Map[String, String], lastKey: Option[String]):
      Map[String, String] = {
      
      unprocessedLines.headOption match {
        case Some(head) => {
          val line = head.trim()
          val tail = unprocessedLines.tail
          
          line match {
            case endOfHeader if line == endOfHeaderMarker => {
              val body = tail.mkString("\n") // TODO: Should we store with original newlines?
              emailMap.updated(Key.Body, body)        
            }
            case keyValueRegex(key, value) => {
              val lastKey = Some(key)
              parseAccumulate(tail, emailMap.updated(key, value), lastKey)
            }
            case emptyKeyRegex(key) => {
              val lastKey = Some(key)
              val value = ""
              parseAccumulate(tail, emailMap.updated(key, value), lastKey)
            }
            case multiLineHeader => {
              lastKey match {
                case Some(key) => {
                  val previousValue = emailMap(key)
                  val newValue = previousValue + " " + line
                  newValue.trim()
                  val isBody = false
                  parseAccumulate(tail, emailMap.updated(key, newValue), lastKey)
                }
                case None => throw ParseException("Found line matching multiheader line but no previous header key or body")
              }
            }
          }
        }
        case None => emailMap
      }
     
    }
    
    parseAccumulate(emailLines, Map.empty[String, String], None)
  }
  
}

case class ParseException(msg: String) extends RuntimeException(msg) {

  def this(msg: String, e: Throwable) {
    this(msg)
    this.initCause(e)
  }
}