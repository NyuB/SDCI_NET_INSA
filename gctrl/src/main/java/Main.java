import api.middleware.ProxyGatewayAPIEndpoint;
import api.ryu.RyuAPIEndpoint;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import api.vim.Vnf;
import api.vim.VimEmuAPIEndpoint;

import java.util.HashMap;
import java.util.Map;
//

//* @author couedrao on 25/11/2019.

//* @project gctrl

//

class Main {
    static boolean run = true;
    static final VimEmuAPIEndpoint vim = new VimEmuAPIEndpoint("http://localhost:5001");
    static final Monitor monitor = new Monitor();
    static final Analyze analyze = new Analyze();
    static final Plan plan = new Plan();
    private static final Execute execute = new Execute();
    static final Knowledge shared_knowledge = new Knowledge();
    private static final boolean log = true;
    private static final MANOAPI manoapi = new MANOAPI();

    private static Map<String,String> parseOptions(String[] cmd, String token, int index){
        Map<String, String> res = new HashMap<>();
        for(int i = index;i<cmd.length;i++){
            String[] option = cmd[i].split(token);
            res.put(option[0],option[1]);
        }
        return res;
    }
    private static Map<String, String> parseOptions(String[] cmd){
        return parseOptions(cmd,"=",0);
    }

    public static void main(String[] args) throws Exception {
        Logger.getRootLogger().setLevel(Level.ERROR);

        Map<String,String> options = parseOptions(args);
        if(options.containsKey("uc")){
            Knowledge.setUseCase(1,options.get("UC"));
        }
        if(options.containsKey("lat")){
            Knowledge.gw_lat_threshold = Double.parseDouble(options.get("lat"));
        }

        shared_knowledge.start();
        Thread.sleep(3000);
        Vnf proxy = manoapi.addProxyVnf(vim,"DC","proxy");
        String dockerIP = proxy.getDocker_network();
        monitor.gw_sensor = new ProxyGatewayAPIEndpoint(dockerIP,8888,monitor.monitoredIP, monitor.monitoredPort);
        Thread thread_m = new Thread(() -> {
            try {
                monitor.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });


        Thread thread_a = new Thread(() -> {
            try {
                analyze.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread thread_p = new Thread(() -> {
            try {
                plan.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        Thread thread_e = new Thread(() -> {
            try {
                execute.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        thread_m.start();
        thread_a.start();
        thread_p.start();
        thread_e.start();

    }

    static void logger(String from, String msg) {
        if (log) {
            switch (from) {
                case "Knowledge":
                    System.out.println("\u001B[1;31m" + "\t[" + from + "] : \t\t" + msg + "\u001B[0m");
                    break;
                case "Monitor":
                    System.out.println("\u001B[1;32m" + "\t[" + from + "] : \t\t" + msg + "\u001B[0m");
                    break;
                case "Analyze":
                    System.out.println("\u001B[1;34m" + "\t[" + from + "] : \t\t" + msg + "\u001B[0m");
                    break;
                case "Plan":
                    System.out.println("\u001B[1;35m" + "\t[" + from + "] : \t\t\t" + msg + "\u001B[0m");
                    break;
                case "Execute":
                    System.out.println("\u001B[1;36m" + "\t[" + from + "] : \t\t" + msg + "\u001B[0m");
                    break;
                default:
                    System.out.println("\t[" + from + "] : \t\t" + msg);
            }

        }
    }
}