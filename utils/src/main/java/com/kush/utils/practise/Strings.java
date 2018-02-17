package com.kush.utils.practise;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class Strings {

    @Test
    public void string1() throws Exception {
        String str1 = "kush";
        String str2 = "kush";
        assertThat(str1 == str2, is(equalTo(true)));
    }

    @Test
    public void string2() throws Exception {
        String str1 = "kush";
        String str2 = new String("kush");
        assertThat(str1 == str2, is(equalTo(false)));
    }

    @Test
    public void string3() throws Exception {
        String str1 = "kush";
        String str3 = new String("kush");
        String str2 = str3.intern();
        assertThat(str1 == str2, is(equalTo(true)));
    }

    @Test
    public void string4() throws Exception {
        String str1 = "kush";
        String str2 = new String(str1);
        assertThat(str1 == str2, is(equalTo(false)));
    }
}
