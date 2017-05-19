package com.gm.a80066158.rmiutil;

import java.util.List;

/**
 * Created by 80066158 on 2017-05-19.
 */

public interface IRMIService {
    public ServerState getServerState();
    public List<Integer> getPrimeNumber(int from, int to);
    public List<Integer> getPrimeNumber(List<Integer> numbers);
}
