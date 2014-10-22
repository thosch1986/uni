import fau.cs7.nwemu.*;

public class NWEmuTest {
  
  public static void main(String[] args) {
    
    class CommonHost extends AbstractHost {
        
      int seqnum = 0;
      int acknum = 0;  
        
    }
 
    class SendingHost extends CommonHost {
    
      public void init() {
        sysLog(0, "Sending Host: init()");                    
        seqnum = 0;
        acknum = -1;
      }  
        
      public Boolean output(NWEmuMsg message) {
        NWEmuPkt sndpkt = new NWEmuPkt();
        for(int i=0; i<NWEmu.PAYSIZE; i++) {
          sndpkt.payload[i] = message.data[i];
        }
        sndpkt.seqnum = seqnum++;
        sndpkt.acknum = acknum;
        sndpkt.checksum = 0;  
        sysLog(0, "Sending Host: output("+message+") -> toLayer3("+sndpkt+")");
        toLayer3(sndpkt);

	return true;
      }
            
      public void input(NWEmuPkt pkt) {
        sysLog(0, "Sending Host: input("+pkt+")");
      }
                
      public void timerInterrupt() {
        sysLog(0, "Sending Host: timerInterrupt()");                    
      }
            
    }
        
    class ReceivingHost extends CommonHost {
        
      public void init() {
        sysLog(0, "Receiving Host: init()");                    
        seqnum = 0;
        acknum = 0;
      }  
      
      public void input(NWEmuPkt pkt) {
        NWEmuMsg message = new NWEmuMsg(); 
        for(int i=0; i<NWEmu.PAYSIZE; i++) {
          message.data[i] = pkt.payload[i];
        }
        sysLog(0, "Receiving Host: input("+pkt+") -> toLayer5("+message+")");
        toLayer5(message);
      }
    }
        
    SendingHost   HostA = new SendingHost();
    ReceivingHost HostB = new ReceivingHost();
    
    NWEmu TestEmu = new NWEmu(HostA, HostB);
    TestEmu.randTimer();  
    TestEmu.emulate(10, 0.0, 0.0, 10.0, 1);
    // send 10 messages, no loss, no corruption, lambda 10, log level 1
  }
}
