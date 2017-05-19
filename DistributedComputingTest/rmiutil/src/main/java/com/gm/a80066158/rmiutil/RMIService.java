package com.gm.a80066158.rmiutil;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import lipermi.exception.LipeRMIException;
import lipermi.handler.CallHandler;
import lipermi.net.IServerListener;
import lipermi.net.Server;

/**
 * Created by 80066158 on 2017-05-19.
 */

public class RMIService implements IRMIService {
    private static final int RMI_PORT = 7777;

    private IProgress progressListener = null;

    public RMIService() {
        try {
            CallHandler callHandler = new CallHandler();
            callHandler.registerGlobal(IRMIService.class, this);
            Server server = new Server();
            server.bind(RMI_PORT, callHandler);
            server.addServerListener(new IServerListener() {

                @Override
                public void clientDisconnected(Socket socket) {
                    System.out.println("Client Disconnected: " + socket.getInetAddress());
                }

                @Override
                public void clientConnected(Socket socket) {
                    System.out.println("Client Connected: " + socket.getInetAddress());
                }
            });
            System.out.println("Server Listening");
        } catch (LipeRMIException | IOException e) {
            e.printStackTrace();
        }
    }

    public void setProgressListener(IProgress progressListener) {
        this.progressListener = progressListener;
    }

    @Override
    public ServerState getServerState() {
        ServerState serverState = new ServerState();
        serverState.setFreeCpu(100);
        serverState.setFreeMemory(100);
        return serverState;
    }

    @Override
    public List<Integer> getPrimeNumber(int from, int to) {
        List<Integer> primeNumList = new ArrayList<>();

        for (int i = from; i <= to; i++) {
            if (isPrimeNumber(i)) {
                primeNumList.add(i);
            }

            if (progressListener != null) {
                progressListener.progressUpdate((float) ((i - from + 1) * 1.0 / (to - from + 1)));
            }

            if ((i - from) % 1000 == 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return primeNumList;
    }

    @Override
    public List<Integer> getPrimeNumber(List<Integer> numbers) {
        List<Integer> primeNumList = new ArrayList<>();

        for (int i = 0; i < numbers.size(); i++) {
            if (isPrimeNumber(numbers.get(i))) {
                primeNumList.add(numbers.get(i));
            }
            if (progressListener != null) {
                progressListener.progressUpdate((float) ((i + 1) * 1.0 / numbers.size()));
            }

            if (i % 1000 == 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        return primeNumList;
    }

    private boolean isPrimeNumber(int num) {
        if (num <= 0) {
            return false;
        }

        for (int i = 2; i <= num / 2; i++) {
            if (num % i == 0) {
                return false;
            }
        }

        return true;
    }
}
