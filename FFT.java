
/**
     A class to multiply two polynomials using the Fast
       Fourier Transform
 */

import java.util.ArrayList;
import java.util.Arrays;


public class FFT
{
    

    /**
         A class for complex numbers
     */
    private class Complex
    {
       // the real and imaginary parts
       private double real;    
       private double im; 
       
       // imaginary parts below this value will be ignored,
       //   so that the complex number will be assumed
       //   to be real
       private double TOLERANCE = 0.00001;
       
       
       /**
           Constructs a complex number given the real
             and imaginary parts.
           @param real the real part
           @param im the imaginary part
        */
       
       public Complex(double real, double im) {
         this.real = real;
         this.im = im;                                   
       }
         
         
       /**
            Constructs the nth root of 1 with smallest 
              positive angle to 1 + 0i.
        */
       
       public Complex(double n) {
         real = Math.cos(n);
         im = Math.sin(n);                     
       }

       
       /**
            Finds the sum of the complex number and another
              complex number
            @param c the other complex number
            @return the sum
        */
       
       public Complex add(Complex c) {
         return new Complex(real+c.real, im+c.im);             
       }
                            

       /**
            Finds the difference of the complex number and another
              complex number
            @param c the other complex number
            @return the result of subtracting the other complex
              number from the complex number
        */
       
       public Complex subtract(Complex c) {
         return new Complex(real-c.real, im-c.im);             
       }                           
         
         
       /**
            Finds the product of the complex number and another
              complex number
            @param c the other complex number
            @return the product
        */
       
       public Complex multiply(Complex c) {
         return new Complex(real*c.real - im*c.im,
                            real*c.im + im*c.real);            
       }
                            
    }
   public ArrayList<Complex> fft( ArrayList<Complex> a){               
     int n = a.size();          
     if (n==1)
       return a;
       
     // split the sequence of coefficients into two pieces  
     ArrayList<Complex> xEven = new ArrayList<Complex>();
     ArrayList<Complex> xOdd = new ArrayList<Complex>();
     for (int i=0; i<n; i=i+2){
       xEven.add(a.get(i));
       xOdd.add(a.get(i+1));                                   
     }
       
     // process each piece recursively
     ArrayList<Complex> yEven = fft(xEven);
     ArrayList<Complex> yOdd = fft(xOdd);   
       
     // create space for the results  
     ArrayList<Complex> y = new ArrayList<Complex>();
     for (int i=0; i<n; i++)
       y.add(new Complex(0,0));

     // combine the recursively processed pieces into
     //   a list of the n desired values     
     for (int k=0; k <n/2 ; k++){
       Complex x = new Complex(k*2*Math.PI/n);  
       y.set(k, yEven.get(k).add(x.multiply(yOdd.get(k))));
       y.set(k+n/2, yEven.get(k).subtract(x.multiply(yOdd.get(k))));                           
     }
     return y;                                                         
   }                                                                                     
}