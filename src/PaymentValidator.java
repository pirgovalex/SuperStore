import javax.swing.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger; //intelij go suggest-na . nqmam threading, ne znaq zashto...
import java.util.stream.*;

public class PaymentValidator {

    public static boolean checkCard(String cardNumber){
        cardNumber = cardNumber.replaceAll("\\s+", "");
        System.out.println("Cleaned Card Number: " + cardNumber);
        ArrayList<Integer>cardArrayList   =   Arrays
                .stream(cardNumber.split(""))
                .map(Integer::parseInt)
                .collect(Collectors.toCollection(ArrayList::new)); //omg stream
        if(cardArrayList.size()==16){
            Integer currentNum = 0 ;
//chatgpt could NEVER make this!!!
            for(int i = 0 ; i<cardArrayList.size(); i++){ //MNOGO mi se spi, that's why this is so weirdly made
                if (i%2!=0){
                    if(cardArrayList.get(i)*2>9) {
                        currentNum = cardArrayList.get(i) * (Integer) 2-9;
                    }
                    else currentNum = cardArrayList.get(i);
                      /*(operatedNumber = currentNum.toString().split("");
                      Arrays.stream(operatedNumber).map(Integer::parseInt).sum();*/

                   cardArrayList.set(i,(Integer)currentNum);

                }
            }

                    int cardDigitsSum = cardArrayList.stream().mapToInt(Integer::intValue).sum();
            System.out.println(cardDigitsSum%10);
            return cardDigitsSum % 10 == 0;
        }
        return false;
    }
    public static boolean checkCVC(String cvc, JFrame parentFrame) {
        if (!cvc.matches("^\\d{3}$")) {
            JOptionPane.showMessageDialog(parentFrame, "Invalid CVC format. Please enter 3 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    public static boolean checkDate (String date, JFrame parentFrame){
        if (!date.matches("^\\d{2}/\\d{2}$")) {
            JOptionPane.showMessageDialog(parentFrame, "Invalid Expiration Date format. Please use MM/YY format.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
