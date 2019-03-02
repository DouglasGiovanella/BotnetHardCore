package model;

import java.io.Serializable;

/**
 * @Author: Douglas A. Giovanella
 * @Email: douglas_giovanella@hotmail.com
 * @Date: 28/07/2018
 */
public class SimplePair<FIRST, SECOND> implements Serializable {

    public FIRST first;

    public SECOND second;

    public SimplePair(FIRST first, SECOND second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public String toString() {
        return "first: " + first + " - second: " + second;
    }
}


