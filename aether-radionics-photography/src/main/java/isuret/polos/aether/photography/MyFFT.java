package isuret.polos.aether.photography;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MyFFT {

    // 1D FFT
    // see: http://www.dspguide.com/ch12/3.htm
    public static void FFT(double[] Re, double[] Im) {
        int N = Re.length;
        int N_2 = N/2;
        int j = N_2;
        int M = (int)Math.round(Math.log(N)/Math.log(2));
        int k;
        double tr, ti;
        // bit reversal sorting
        for(int i = 1; i < N-1; i++) {
            if(i < j) {
                tr = Re[j];
                Re[j] = Re[i];
                Re[i] = tr;
                tr = Im[j];
                Im[j] = Im[i];
                Im[i] = tr;
            }
            k = N_2;
            while(k <= j) {
                j -= k;
                k /= 2;
            }
            j += k;
        }
        int le = 1;
        int le_2, jm1;
        int Nm1 = N-1;
        double ur, ui, sr, si;
        // frequency domain synthesis
        for(int l = 1; l <= M; l++) {
            le_2 = le;
            le *= 2;
            ur = 1.0;
            ui = 0.0;
            sr = Math.cos(Math.PI/le_2);
            si = -Math.sin(Math.PI/le_2);
            for(j = 1; j <= le_2; j++) {
                jm1 = j-1;
                for(int i = jm1; i <= Nm1; i += le) {
                    k = i+le_2;
                    tr = Re[k]*ur-Im[k]*ui;
                    ti = Re[k]*ui+Im[k]*ur;
                    Re[k] = Re[i]-tr;
                    Im[k] = Im[i]-ti;
                    Re[i] += tr;
                    Im[i] += ti;
                }
                tr = ur;
                ur = tr*sr-ui*si;
                ui = tr*si+ui*sr;
            }
        }
    }

    // 1D IFFT
    // see: http://www.dspguide.com/ch12/3.htm
    public static void IFFT(double[] Re, double[] Im) {
        int N = Re.length;
        for(int i = 0; i < N; i++) {
            Im[i] = -Im[i];	// complex conjugate
        }
        FFT(Re, Im);
        for(int i = 0; i < N; i++) {
            Im[i] = -Im[i];	// complex conjugate
        }
    }

    // 2D FFT
    public static void FFT2D(double[] Re, double[] Im, int N, int M) {
        double[] re = new double[N];
        double[] im = new double[N];
        int k = 0;
        for(int j = 0; j < M; j++) {	// 1D FFT the rows
            for(int i = 0; i < N; i++) {	// copy row to temporary arrays
                re[i] = Re[k];
                im[i] = Im[k++];
            }
            FFT(re, im);
            k = j*N;
            for(int i = 0; i < N; i++) {	// copy back
                Re[k] = re[i];
                Im[k++] = im[i];
            }
        }
        if(N != M) {
            re = new double[M];
            im = new double[M];
        }
        for(int i = 0; i < N; i++) {	// 1D FFT the columns
            k = i;
            for(int j = 0; j < M; j++) {
                re[j] = Re[k];
                im[j] = Im[k];
                k += N;
            }
            FFT(re, im);
            k = i;
            for(int j = 0; j < M; j++) {
                Re[k] = re[j];
                Im[k] = im[j];
                k += N;
            }
        }
    }

    // 2D IFFT
    public static void IFFT2D(double[] Re, double[] Im, int N, int M) {
        double[] re = new double[N];
        double[] im = new double[N];
        int k = 0;
        for(int j = 0; j < M; j++) {
            for(int i = 0; i < N; i++) {
                re[i] = Re[k];
                im[i] = Im[k++];
            }
            IFFT(re, im);
            k = j*N;
            for(int i = 0; i < N; i++) {
                Re[k] = re[i];
                Im[k++] = im[i];
            }
        }
        if(N != M) {
            re = new double[M];
            im = new double[M];
        }
        for(int i = 0; i < N; i++) {
            k = i;
            for(int j = 0; j < M; j++) {
                re[j] = Re[k];
                im[j] = Im[k];
                k += N;
            }
            IFFT(re, im);
            k = i;
            for(int j = 0; j < M; j++) {
                Re[k] = re[j];
                Im[k] = im[j];
                k += N;
            }
        }
    }

    // determines the extrema of the given data
    public static double extremum(double[] Re, double[] Im, int N, int M, boolean real, boolean min) {
        int k = 0;
        double magnitude, result;
        if(min) result = 1e100;
        else result = -1e100;
        for(int j = 0; j < M; j++) {
            for(int i = 0; i < N; i++) {
                magnitude = Re[k];
                if(!real) {
                    magnitude *= Re[k];
                    magnitude += Im[k]*Im[k];
                    magnitude = Math.sqrt(magnitude);
                }
                if(min) {
                    if(magnitude < result) result = magnitude;
                }
                else if(magnitude > result) result = magnitude;
                k++;
            }
        }
        return result;
    }

    // scales the given data with 1/(N*M)
    public static void scale(double[] Re, double[] Im, int N, int M) {
        int NM = N*M;
        double iNM = 1.0/NM;
        for(int k = 0; k < NM; k++) {
            Re[k] *= iNM;
            Im[k] *= iNM;
        }
    }

    // performs linear mapping on x
    public static double map(double x, double min0, double max0, double min, double max) {
        return ((min-max)*x+(max*min0-max0*min))/(min0-max0);
    }

    // writes the given data into an image file
    public static void draw(double[] Re, double[] Im, double min, double max, int N, int M, boolean real, boolean log, String name) throws IOException {
        BufferedImage image = new BufferedImage(N, M, BufferedImage.TYPE_INT_RGB);
        int k = 0;
        double H, S, B, magnitude;
        for(int j = 0; j < M; j++) {
            for(int i = 0; i < N; i++) {
                magnitude = Re[k];
                if(!real) {
                    H = map(Math.atan2(Im[k], Re[k]), -Math.PI, Math.PI, 0.0, 1.0);
                    S = 1.0;
                    magnitude *= Re[k];
                    magnitude += Im[k]*Im[k];
                    magnitude = Math.sqrt(magnitude);
                } else {
                    H = 0.0;
                    S = 0.0;
                }
                if(log) B = map(Math.log(1.0+magnitude), 0.0, Math.log(1.0+max), 0.0, 1.0); // magnitude >= 0 !!!
                else B = map(magnitude, min, max, 0.0, 1.0);
                image.setRGB(i, M-j-1, Color.HSBtoRGB((float)H, (float)S, (float)B));
                k++;
            }
        }
        ImageIO.write(image, "png", new File(name+".png"));
    }
}

