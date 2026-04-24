package com.sumankumar.chatapp.signup;

public class SignUpDataVerification {

    static int at_rate_count=0;


    //final email check
    public static boolean final_email_check(String email){
        String username="";
        try {
            if(email.contains("@"))
                username = email.substring(0, email.lastIndexOf('@'));

        }catch (StringIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return username.length() >= 2;
    }

    public static boolean email(String e) {
        int dotcount = 0;
        at_rate_count=0;
        boolean email_bool = false;

        for (int i = 0; i < e.length(); i++) {
            if ((e.charAt(i) >= 48 && e.charAt(i) <= 57) || (e.charAt(i) >= 65 && e.charAt(i) <= 90) || (e.charAt(i) >= 97 && e.charAt(i) <= 122)
                    || e.charAt(i) == '.' || e.charAt(i) == '@' || e.length() == 0) {

                //at the rate '@' count
                if (e.charAt(i) == '@') {
                    at_rate_count+= 1;
                    if (at_rate_count >=2)
                        return false;
                    else
                        continue;
                }

                //dot count
                if (e.charAt(i) == '.') {
                    dotcount += 1;
                    if (dotcount >= 2)
                        return false;
                    else
                        continue;
                } else {
                    email_bool = true;
                    dotcount = 0;
                }
            } else
                return false;
        }
        return email_bool;
    }

    //password verification
    static boolean password(String p) {
        int u_count = 0, l_count = 0, d_count = 0, s_count = 0;
        int count = 0;
        for (int i = 0; i < p.length(); i++) {

            if (Character.isAlphabetic(p.charAt(i))) {
                if (Character.isUpperCase(p.charAt(i)) && u_count == 0) {
                    count += 1;
                    u_count += 1;
                }
                if (Character.isLowerCase(p.charAt(i)) && l_count == 0) {
                    count += 1;
                    l_count += 1;
                }
            } else if (Character.isDigit(p.charAt(i)) && d_count == 0) {
                count += 1;
                d_count += 1;
            } else if (((p.charAt(i) > 32) && (p.charAt(i) <= 47) && (s_count == 0)) || ((p.charAt(i) >= 58) && (p.charAt(i) <= 64) && (s_count == 0))||
                    ((p.charAt(i) >= 91) && (p.charAt(i) <= 96) && (s_count == 0))||((p.charAt(i) >= 123) && (p.charAt(i) <= 126) && (s_count == 0))) {
                count += 1;
                s_count += 1;
            }
        }
        return count == 4;
    }
}
