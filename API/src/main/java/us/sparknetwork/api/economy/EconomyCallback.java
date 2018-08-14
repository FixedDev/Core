package us.sparknetwork.api.economy;

import lombok.Getter;

public class EconomyCallback {

    private EconTransaction callback;
    @Getter
    private double oldBalance;
    @Getter
    private double newBalance;
    @Getter
    private ErrorReason error;

    public EconomyCallback(EconTransaction e, double balance, double newbalance, ErrorReason errormsg) {
        this.callback = e;
        this.oldBalance = balance;
        this.newBalance = newbalance;
        this.error = errormsg;
    }

    public EconTransaction isSucessfully() {
        return callback;
    }

    public enum EconTransaction {

        SUCCESSFULLY, FAILED
    }

    public enum ErrorReason {
        EVENTCANCELLED, USERDONTEXIST, INVALIDBALANCE, NOERRORS
    }

    public static EconomyCallback getIBCallback(double balance){
        return new EconomyCallback(EconTransaction.FAILED, balance, balance, ErrorReason.INVALIDBALANCE);
    }

    public static EconomyCallback getECCallback(){
        return new EconomyCallback(EconTransaction.FAILED, 0, 0, ErrorReason.EVENTCANCELLED);
    }

    public static EconomyCallback getUDECallback(){
        return new EconomyCallback(EconTransaction.FAILED, 0 ,0, ErrorReason.USERDONTEXIST);
    }

}
