package kipsigman.enron

import scala.io.Source

import org.scalatest.Matchers
import org.scalatest.WordSpec

class EmailParserSpec extends WordSpec with Matchers {
  
  val sampleEmail = """Message-ID: <28898023.1075859469537.JavaMail.evans@thyme>
Date: Fri, 28 Dec 2001 09:04:54 -0800 (PST)
From: announcements.enron@enron.com
To: dl-ga-all_enron_houston_employees@enron.com
Subject: Metro Bus Passes and Woodlands Express Passes Available Effective
  Friday, December 28, 2001
Mime-Version: 1.0
Content-Type: text/plain; charset=us-ascii
Content-Transfer-Encoding: 7bit
X-From: Enron General Announcements </O=ENRON/OU=NA/CN=RECIPIENTS/CN=MBX_ANNCENRON>
X-To: DL-GA-all_enron_houston_employees </O=ENRON/OU=NA/CN=RECIPIENTS/CN=DL-GA-all_enron_houston_employees>
X-cc: 
X-bcc: 
X-Folder: \Susan_Bailey_Jan2002\Bailey, Susan\Inbox
X-Origin: Bailey-S
X-FileName: sbaile2 (Non-Privileged).pst


Enron employees not participating in contract parking are eligible to receive Metro Bus Passes or Woodlands Express Passes.  You may pick up a bus pass from the Parking & Transportation Desk, on Level 3 of the Enron Building, from 8:30 AM to 4:30 PM. 


All Metro passes offered through the Enron Parking & Transportation desk will be the Metro 30 Day Zone Pass.  When you use a 30-day zone pass, bus service is divided into four zones w/ unlimited rides.  The zone pass is time activated, which means that it will not become active until the first time it is used and will not expire until 30 days after it was used for the first time.  New passes will be available to Enron employees upon expiration of the 30 day time period. Each zone does have a different fare values based on the distance the bus travels. This is important to any buser who wants to transfer to or travel in a higher-cost zone than the zone pass they have, in this case the difference must be paid in cash.  If they are traveling in a lower cost zone, then there is no extra cost.  

Parking & Transportation Desk
3-7060"""
  
  "parse" should {
    "return Map of key/values for email string" in {
      val emailMap = EmailParser.parse(sampleEmail)
      
      //println(emailMap)
      //println(emailMap(EmailParser.Key.Body))
      
      emailMap(EmailParser.Key.From) shouldEqual "announcements.enron@enron.com"
      emailMap(EmailParser.Key.Subject) shouldEqual "Metro Bus Passes and Woodlands Express Passes Available Effective Friday, December 28, 2001"
    }
    "return Map of key/values for email lines" in {
      
      val emailLines = Source.fromURL(getClass.getResource("/maildir/crandell-s/inbox/99.")).getLines().toSeq
      val emailMap = EmailParser.parse(emailLines)
      
      emailMap(EmailParser.Key.From) shouldEqual "fran.chang@enron.com"
      emailMap(EmailParser.Key.Cc) shouldEqual "john.postlethwaite@enron.com, william.crooks@enron.com, nikolay.kraltchev@enron.com"
    }
  }  
}