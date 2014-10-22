package fau.cs7.nwemu;

public class NWEmuMsg implements Cloneable {
    public byte data[] = new byte[NWEmu.PAYSIZE];
}   

public class NWEmuPkt implements Cloneable {
    public int seqnum = 0;
    public int acknum = 0;
    public int flags = 0;
    public int checksum = 0;
    public byte payload[] = new byte[NWEmu.PAYSIZE];
}

public class AbstractHost {

    public Boolean output(NWEmuMsg message) {
        System.out.println(descriptor+".output() not implemented, data = " + new String(message.data));
    }
  
    public void input(NWEmuPkt packet) {
        System.out.println(descriptor+".input() not implemented, payload = " + new String(packet.payload));
    }
  
    public void init() {
        System.out.println(descriptor+".init() not implemented");
    }
        
    public void timerInterrupt() {
        System.out.println(descriptor+".timerInterrupt() not implemented");
    }
    
    public final void startTimer(double delay);
    
    public final boolean stopTimer();
    
    public final void toLayer3(NWEmuPkt packet);
    
    public final void toLayer5(NWEmuMsg message);

    public final double currTime();
        
    public final void sysLog(int level, String str);

}

public class NWEmu {
    
    public final static int PAYSIZE = 20;

    public NWEmu(AbstractHost ha, AbstractHost hb);

    public void randTimer();

    public void emulate(int    nsimmax,
                        double lossprob,
                        double corruptprob,
                        double lambda,
                        int    newtrace);
                       
}
