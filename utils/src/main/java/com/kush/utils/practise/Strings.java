package com.kush.utils.practise;

import org.junit.Test;

public class Strings {

    @Test
    public void strings() throws Exception {

        String str1 = "kush";
        String str2 = "kush";
        String str3 = new String("kush");
        String str4 = str3.intern();
        String str5 = new String(str4);

        System.out.println(str1 == str2);
        System.out.println(str2 == str3);
        System.out.println(str3 == str4);
        System.out.println(str4 == str1);
        System.out.println(str5 == str4);
    }
}
