package ro.info.uaic.movierecommendation.hashing;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hashing {

    public static String doHashing(String password){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");

            messageDigest.update(password.getBytes());

            byte[] resultByteArray = messageDigest.digest();

            StringBuilder builder = new StringBuilder();

            for(byte currentByte : resultByteArray){
                builder.append(String.format("%02x", currentByte));
            }

            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }
}
