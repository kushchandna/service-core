package com.kush.utils.practise;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class Integers {

    @Test
    public void integers1() throws Exception {
        Integer int1 = 20;
        Integer int2 = 20;
        assertThat(int1 == int2, is(equalTo(true)));
    }

    @Test
    public void integer2() throws Exception {
        Integer int1 = 200;
        Integer int2 = 200;
        assertThat(int1 == int2, is(equalTo(false)));
    }

    @Test
    public void integer3() throws Exception {
        Integer int1 = new Integer(20);
        Integer int2 = 20;
        assertThat(int1 == int2, is(equalTo(false)));
    }

    @Test
    public void integer4() throws Exception {
        Integer int1 = 20;
        int int2 = 20;
        assertThat(int1 == int2, is(equalTo(true)));
    }

    @Test
    public void integer5() throws Exception {
        Integer int1 = 200;
        int int2 = 200;
        assertThat(int1 == int2, is(equalTo(true)));
    }

    @Test
    public void integer6() throws Exception {
        Integer int1 = new Integer(20);
        int int2 = 20;
        assertThat(int1 == int2, is(equalTo(true)));
    }
}
