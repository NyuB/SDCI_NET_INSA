package api.gates;

public class PingResponse {

    private long pong ;

    public PingResponse() {}

    public void setPong(int pong) {
        this.pong = pong ;
    }    

    public long getPong() {
        return this.pong ;
    } 
}