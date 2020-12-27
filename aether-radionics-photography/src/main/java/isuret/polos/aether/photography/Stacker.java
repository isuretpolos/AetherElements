package isuret.polos.aether.photography;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Stacker {

    public static Random rng = new Random();
    public static double a_pi2 = 2.0/(Math.PI*Math.PI);
    public static double pi_a = Math.PI/2.0;
    public static double tpi = 2.0*Math.PI;
    public static double pi2 = Math.PI*Math.PI;

    // if argument is within bounds
    public static boolean valid(int x, int max) {
        if(x >= 0 && x < max) return true;
        else return false;
    }

    // returns array value
    public static double f(int i, int j, int w, int h, double[] array, boolean PBC) {
        if(valid(i, w) && valid(j, h)) {
            return array[i+w*j];
        } else if(PBC) {
            i = (i+1024*w)%w;
            j = (j+1024*h)%h;
            return array[i+w*j];
        }
        return 0.0;
    }

    // polynomial for bicubic interpolation
    public static double p(double t, double a1, double a2, double a3, double a4) {
        double t2 = t*t;
        double t3 = t2*t;
        double s1 = -t+2.0*t2-t3;
        double s2 = 2-5.0*t2+3.0*t3;
        double s3 = t+4*t2-3*t3;
        double s4 = -t2+t3;
        return s1*a1+s2*a2+s3*a3+s4*a4;
    }

    // bicubic interpolation
    public static double bicubic(double[] array, int w, int h, double x, double y, boolean PBC) {
        double[][] a = new double[4][4];
        double tx = x-Math.floor(x);
        double ty = y-Math.floor(y);
        int ia = (int)Math.floor(x)-1;
        int ja = (int)Math.floor(y)-1;
        for(int j = 0; j < 4; j++) {
            for(int i = 0; i < 4; i++) {
                a[i][j] = f(ia+i, ja+j, w, h, array, PBC);
            }
        }
        return p(ty, p(tx, a[0][0], a[1][0], a[2][0], a[3][0]),
                p(tx, a[0][1], a[1][1], a[2][1], a[3][1]),
                p(tx, a[0][2], a[1][2], a[2][2], a[3][2]),
                p(tx, a[0][3], a[1][3], a[2][3], a[3][3]));
    }

    // resamples the given data
    public static void resample(int w1, int h1, double[] first, int v, int g, int w2, int h2, double[] second, double x0, double y0, double resolution, double tilt, boolean additive) {
        double ux = resolution*Math.cos(tilt);
        double uy = resolution*Math.sin(tilt);
        int n0 = 0;
        int n;
        double x, y, s;
        for(int j = 0; j < g; j++) {
            x = x0;
            y = y0;
            n = n0;
            for(int i = 0; i < v; i++) {
                s = bicubic(first, w1, h1, x, y, false);
                if(additive) second[n] += s;
                else second[n] = s;
                x += ux;
                y += uy;
                n++;
            }
            x0 -= uy;
            y0 += ux;
            n0 += w2;
        }
    }

    // copies the given data from one array to another
    public static void copy_array(double[] first, double[] second, int w, int h) {
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                second[k] = first[k];
                k++;
            }
        }
    }

    // determines the extrema of the given data
    public static double[] extrema(double[] re, double[] im, int w, int h, boolean real) {
        int k = 0;
        double min = 1e100;
        double max = -1e100;
        double magnitude;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                magnitude = re[k];
                if(!real) {
                    magnitude *= re[k];
                    magnitude += im[k]*im[k];
                    magnitude = Math.sqrt(magnitude);
                }
                if(magnitude < min) min = magnitude;
                else if(magnitude > max) max = magnitude;
                k++;
            }
        }
        return new double[] {min, max};
    }

    // 1D Hann window funcion
    public static double Hann(int n, int N) {
        if(valid(n, N)) return 0.5-0.5*Math.cos(tpi*n/(N-1));
        return 0.0;
    }

    // 2D Hann window
    public static void window(double[] array, int w, int h) {
        double[] wx = new double[w];
        double[] wy = new double[h];
        for(int i = 0; i < w; i++) {
            wx[i] = Hann(i, w);
        }
        for(int j = 0; j < h; j++) {
            wy[j] = Hann(j, h);
        }
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                array[k] = wx[i]*wy[j]*array[k];
                k++;
            }
        }
    }

    // returns the center of mass of the thresholded raster
    public static int[] center_of_mass(Raster r, int t) {
        int x = 0;
        int y = 0;
        int m = 0;
        int a;
        int w = r.getWidth();
        int h = r.getHeight();
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                a = r.getSample(i, j, 0)&0xff;
                if(a >= t) {
                    x += i;
                    y += j;
                    m++;
                }
            }
        }
        if(x < 0 || y < 0) throw new RuntimeException("Integer overflow!");
        if(m == 0) throw new RuntimeException("Failed to determine the center of mass!");
        return new int[] {(int)((double)x/m), (int)((double)y/m)};
    }

    // computes the cross-power spectrum between the two given sets of data
    public static void cross_power(double[] re1, double[] im1, double[] re2, double[] im2, int w, int h) {
        int k = 0;
        double a, b, c, d, re, im, abs;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                a = re1[k];
                b = im1[k];
                c = re2[k];
                d = -im2[k];
                re = a*c-b*d;
                im = a*d+b*c;
                abs = re*re+im*im;
                if(abs == 0.0) {
                    re2[k] = 0.0;
                    im2[k] = 0.0;
                } else {
                    abs = Math.sqrt(abs);
                    re2[k] = re/abs;
                    im2[k] = im/abs;
                }
                k++;
            }
        }
    }

    // determines the location of the maximum in the given data array
    public static double[] peak(double[] array, int w, int h) {
        int i, j, i0 = 0, j0 = 0;
        int k = 0;
        double max = -1e100;
        double[] result = new double[2];
        for(j = 0; j < h; j++) {
            for(i = 0; i < w; i++) {
                if(array[k] > max) {
                    max = array[k];
                    i0 = i;
                    j0 = j;
                }
                k++;
            }
        }
        double r0, r1, r2, min;
        if(i0 > 0) r0 = array[i0-1+w*j0]; else r0 = array[w-1+w*j0];
        r1 = array[i0+w*j0];
        if(i0 < w-1) r2 = array[i0+1+w*j0]; else r2 = array[w*j0];
        if(r0 < r2) min = r0; else min = r2;
        result[0] = i0+0.5*(r2-r0)/(r1-min);
        if(result[0] >= 0.5*w) result[0] -= w;
        if(j0 > 0) r0 = array[i0+w*(j0-1)]; else r0 = array[i0+w*(h-1)];
        r1 = array[i0+w*j0];
        if(j0 < h-1) r2 = array[i0+w*(j0+1)]; else r2 = array[i0];
        if(r0 < r2) min = r0; else min = r2;
        result[1] = j0+0.5*(r2-r0)/(r1-min);
        if(result[1] >= 0.5*h) result[1] -= h;
        return result;
    }

    // converts an raster array to a double array
    public static void raster2array(Raster raster, double[] array, int i0, int j0, int w, int h) {
        int k = 0;
        for(int j = h-1; j >= 0; j--) {
            for(int i = 0; i < w; i++) {
                if(valid(i0+i, raster.getWidth()) && valid(j0+j, raster.getHeight()))
                    array[k] = raster.getSample(i0+i, j0+j, 0)&0xff;
                else array[k] = 0.0;
                k++;
            }
        }
    }

    // crops the given data
    public static void crop(double[] first, int W, int H, double[] second, int i0, int j0, int w, int h) {
        int k, l = 0;
        for(int j = 0; j < h; j++) {
            k = i0+W*(j0+j);
            for(int i = 0; i < w; i++) {
                second[l] = first[k];
                k++;
                l++;
            }
        }
    }

    // performs a log-polar transform on the given data
    public static void log_pol(double[] re, double[] im, int w, int h, double R, double[] lp, int w2, int h2, double a, double b, double u, double v, boolean PBC, boolean window) {
        int k = 0;
        double[] magnitude = new double[w*h];
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                magnitude[k] = Math.sqrt(re[k]*re[k]+im[k]*im[k]);
                k++;
            }
        }
        double[] wr = null;
        if(window) {
            wr = new double[w2];
            for(int i = 0; i < w2; i++) {
                wr[i] = Hann(i, w2);
            }
        }
        k = 0;
        double phi, r, x, y, cos, sin;
        for(int j = 0; j < h2; j++) {
            phi = Math.PI*j/h2;
            cos = Math.cos(phi);
            sin = Math.sin(phi);
            for(int i = 0; i < w2; i++) {
                r = a*Math.exp(b*i);
                if(r < R) {
                    x = r*cos;
                    y = r*sin;
                    lp[k] = bicubic(magnitude, w, h, u+x, v+y, PBC);
                    if(window) lp[k] *= wr[i];
                } else lp[k] = 1.0e6;
                k++;
            }
        }
    }

    // linearly maps x from [min0, max0] to [min, max]
    public static double map(double x, double min0, double max0, double min, double max) {
        return ((min-max)*x+(max*min0-max0*min))/(min0-max0);
    }

    // performs gamma correction on the given data
    public static void gamma(double[] array, int w, int h, double[] minmax, double g) {
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                array[k] = Math.pow(map(array[k], minmax[0], minmax[1], 0.0, 1.0), g);
                k++;
            }
        }
        minmax[0] = 0.0;
        minmax[1] = 1.0;
    }

    // performs a Gaussian convolution on the given data
    public static void Gaussian_convolution(double[] array, int w, int h, double dev) {
        double[] im = new double[w*h];
        MyFFT.FFT2D(array, im, w, h);
        double[] G = new double[w*h];
        int k = 0;
        double a = -0.5/(dev*dev);
        double rx2, ry2;
        for(int j = 0; j < h; j++) {
            if(j < h/2) ry2 = j;
            else ry2 = j-h;
            ry2 *= a*ry2;
            for(int i = 0; i < w; i++) {
                if(i < w/2) rx2 = i;
                else rx2 = i-w;
                rx2 *= a*rx2;
                G[k] = Math.exp(rx2+ry2);
                k++;
            }
        }
        MyFFT.FFT2D(G, new double[w*h], w, h);
        k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                array[k] *= G[k];
                im[k] *= G[k];
                k++;
            }
        }
        MyFFT.IFFT2D(array, im, w, h);
    }

    // diffraction limits the given data
    // see: http://photo.net/learn/optics/lensTutorial#part5
    public static void diffraction_limit(double[] array, int w, int h, double cutoff) {
        double[] im = new double[w*h];
        MyFFT.FFT2D(array, im, w, h);
        double[] OTF = new double[w*h];
        int k = 0;
        double a = 2.0/Math.PI;
        double b = 1.0/cutoff;
        double r, rx, ry;
        for(int j = 0; j < h; j++) {
            if(j < h/2) ry = j;
            else ry = j-h;
            for(int i = 0; i < w; i++) {
                if(i < w/2) rx = i;
                else rx = i-w;
                r = Math.sqrt(rx*rx+ry*ry);
                if(r < cutoff) {
                    r *= b;
                    OTF[k] = a*(Math.acos(r)-r*Math.sqrt(1-r*r));
                } else OTF[k] = 0.0;
                k++;
            }
        }
        k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                array[k] *= OTF[k];
                im[k] *= OTF[k];
                k++;
            }
        }
        MyFFT.IFFT2D(array, im, w, h);
    }

    // normalizes the given data
    public static void normalize(double[] array, int w, int h) {
        double divA = 1.0/(w*h);
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                array[k] *= divA;
                k++;
            }
        }
    }

    // "normalizes" the given data arbitrarily
    public static void normalize(double[] array, int w, int h, double min, double max) {
        double[] minmax = extrema(array, null, w, h, true);
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                array[k] = map(array[k], minmax[0], minmax[1], min, max);
                k++;
            }
        }
    }

    // saturates the given data
    public static void saturate(double[] array, int w, int h, double min, double max) {
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                if(array[k] < min) array[k] = min;
                else if(array[k] > max) array[k] = max;
                k++;
            }
        }
    }

    // adds noise to the given data
    public static void add_noise(double[] array, int w, int h, double sigma) {
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                array[k++] += sigma*rng.nextGaussian();
            }
        }
    }

    // high-boost filters the given data
    public static void high_boost_filter(double[] array, int w, int h, double x, double dev) {
        double[] low = new double[w*h];
        copy_array(array, low, w, h);
        Gaussian_convolution(low, w, h, dev);
        normalize(array, w, h);
        normalize(low, w, h);
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                array[k] = x*array[k]-low[k];
                k++;
            }
        }
    }

    // constructs the histogram of the given image
    public static double[] histogram(double[] array, int w, int h, double[] minmax, int resolution) {
        int[] bins = new int[resolution];
        int k = 0;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
                bins[(int)map(array[k], minmax[0], minmax[1], 0.0, resolution-1e-6)]++;
                k++;
            }
        }
        double iwh = 1.0/(w*h);
        double[] histogram = new double[resolution];
        for(int i = 0; i < resolution; i++) {
            histogram[i] = bins[i]*iwh;
        }
        return histogram;
    }

    // computes the entropy of the given image
    public static double entropy(double[] histogram) {
        double entropy = 0.0;
        double ilog2 = 1.0/Math.log(2.0);
        for(int i = 0; i < histogram.length; i++) {
            if(histogram[i] != 0.0) entropy -= histogram[i]*Math.log(histogram[i])*ilog2;
        }
        return entropy;
    }

    // writes the given data into an image file
    public static void draw_array(double[] Re, double[] Im, double min, double max, int w, int h, boolean real, boolean log, String name) throws IOException {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int k = 0;
        double H, S, B, magnitude;
        for(int j = 0; j < h; j++) {
            for(int i = 0; i < w; i++) {
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
                if(log) B = map(Math.log(1.0+magnitude), Math.log(1.0+min), Math.log(1.0+max), 0.0, 1.0);
                else B = map(magnitude, min, max, 0.0, 1.0);
                image.setRGB(i, h-j-1, Color.HSBtoRGB((float)H, (float)S, (float)B));
                k++;
            }
        }
        ImageIO.write(image, "png", new File(name));
    }

    // loads an image
    public static void load_image(String name, double[] array, int w, int h) throws IOException {
        Raster raster = ImageIO.read(new File(name)).getData();
        raster2array(raster, array, 0, 0, w, h);
        normalize(array, w, h, 0.0, 1.0);
    }

    // creates a scaled, rotated, blurred and noisy sample image from a higher-resolution one
    public static void take_sample(double[] first, double[] second, int W, int H, int w, int h, double x0, double y0, double resolution, double tilt, double cutoff, double noise) throws IOException {
        resample(W, H, first, w, h, w, h, second, x0, y0, resolution, tilt*Math.PI/180.0, false);
        diffraction_limit(second, w, h, cutoff);
        normalize(second, w, h, 0.0, 1.0);
        add_noise(second, w, h, noise);
        saturate(second, w, h, 0.0, 1.0);
    }

    // realigns, derotates and descales moon2 to conform to moon1
    // see http://dkfu9dpc.c4-suncomet.com/petrihirvonen/Projects/Stacker/stacker.php for how the algorithm works
    public static double[][] align(double[] moon1, double[] moon2, int w0, int h0, int w, int h, double R, int r, int p, double a, int i) throws IOException {
        double[] re1 = new double[w*h];	// real part of first image
        double[] im1 = new double[w*h];	// imaginary part
        double[] re2 = new double[w*h];	// second image
        double[] im2 = new double[w*h];
        double[] re3 = new double[w*h];
        double[] re4 = new double[w*h];
        double[] lpre1 = new double[r*p];
        double[] lpim1 = new double[r*p];
        double[] lpre2 = new double[r*p];
        double[] lpim2 = new double[r*p];
        double x0 = 0.5*w0-0.5*w;
        double y0 = 0.5*h0-0.5*h;
        resample(w0, h0, moon1, w, h, w, h, re1, x0, y0, 1.0, 0.0, false);
        resample(w0, h0, moon2, w, h, w, h, re2, x0, y0, 1.0, 0.0, false);
        normalize(re1, w, h, 0.0, 1.0);
        normalize(re2, w, h, 0.0, 1.0);
        copy_array(re1, re3, w, h);
        MyFFT.FFT2D(re1, im1, w, h);
        MyFFT.FFT2D(re2, im2, w, h);
        double[] minmax = extrema(re2, im2, r, p, false);
        log_pol(re1, im1, w, h, R, lpre1, r, p, 1.0, a, 0.0, 0.0, true, true);
        log_pol(re2, im2, w, h, R, lpre2, r, p, 1.0, a, 0.0, 0.0, true, true);
        minmax = extrema(lpre2, null, r, p, true);
        MyFFT.FFT2D(lpre1, lpim1, r, p);
        MyFFT.FFT2D(lpre2, lpim2, r, p);
        cross_power(lpre1, lpim1, lpre2, lpim2, r, p);
        minmax = extrema(lpre2, lpim2, r, p, false);
        MyFFT.IFFT2D(lpre2, lpim2, r, p);
        minmax = extrema(lpre2, null, r, p, true);

        double[] temp = new double[2];
        temp = peak(lpre2, r, p);
        double res = 1.0/Math.exp(-a*temp[0]);
        double tilt = -temp[1]/h*Math.PI;
        double cos = Math.cos(tilt);
        double sin = Math.sin(tilt);
        x0 = 0.5*w0-0.5*w*res*cos+0.5*h*res*sin;
        y0 = 0.5*h0-0.5*w*res*sin-0.5*h*res*cos;
        resample(w0, h0, moon2, w, h, w, h, re2, x0, y0, res, tilt, false);
        normalize(re2, w, h, 0.0, 1.0);
        copy_array(re2, re4, w, h);
        im2 = new double[w*h];
        MyFFT.FFT2D(re2, im2, w, h);
        cross_power(re1, im1, re2, im2, w, h);
        MyFFT.IFFT2D(re2, im2, w, h);
        temp = peak(re2, w, h);
        resample(w, h, re4, w, h, w, h, re2, -temp[0], -temp[1], 1.0, 0.0, false);
        normalize(re2, w, h, 0.0, 1.0);
        return new double[][] {re3, re2};
    }

    public static void main(String[] args) throws IOException {
        // log-pol
        // a: cutoff = w0/0.001, A = 0.0
        // b: cutoff = w0/3.0, A = 0.0
        // c: cutoff = w0/3.0, A = 0.05
        // d: cutoff = w0/0.001, A = 0.05
        int W = 5000;
        int H = 5000;
        int w0 = 2048;
        int h0 = 2048;
        int w = 512;
        int h = 512;
        int r = 512;
        int p = 512;
        double cutoff = w0/0.001;
        double A = 0.05;
        double[] moon = new double[W*H];
        String source = "/Users/Pete/Desktop/moon2/moon.png";

        load_image(source, moon, W, H);
        double[] moon1 = new double[w0*h0];
        double[] moon2 = new double[w0*h0];
        double[] stack = new double[w0*h0];

        double res1 = 3.0*Math.sqrt(2.0)*Math.E;
        double tilt1 = 0.0;
        double cos1 = Math.cos(tilt1/180.0*Math.PI);
        double sin1 = Math.sin(tilt1/180.0*Math.PI);
        double x01 = 0.5*W-0.5*w0*res1*cos1+0.5*h0*res1*sin1;
        double y01 = 0.5*H-0.5*w0*res1*sin1-0.5*h0*res1*cos1;
        take_sample(moon, moon1, W, H, w0, h0, x01, y01, res1, tilt1, cutoff, A);

        for(int i = 1; i <= 1000; i++) {

            double res2 = res1+0.5*res1*(rng.nextDouble()-0.5);
            double tilt2 = 180.0*(rng.nextDouble()-0.5);
            double cos2 = Math.cos(tilt2/180.0*Math.PI);
            double sin2 = Math.sin(tilt2/180.0*Math.PI);
            double rx = 500.0*(rng.nextDouble()-0.5);
            double ry = 500.0*(rng.nextDouble()-0.5);
            double x02 = 0.5*W-0.5*w0*res2*cos2+0.5*h0*res2*sin2+rx;
            double y02 = 0.5*H-0.5*w0*res2*sin2-0.5*h0*res2*cos2+ry;

            take_sample(moon, moon2, W, H, w0, h0, x02, y02, res2, tilt2, cutoff, A);
            double[] minmax = extrema(moon2, null, w0, h0, true);
            draw_array(moon2, null, minmax[0], minmax[1], w0, h0, true, false, "/Users/Pete/Desktop/moon2/d-sample-"+i+".png");
            double[][] aligned = align(moon1, moon2, w0, h0, w, h, 255.0, r, p, 0.01084, i);

            resample(w, h, aligned[1], w0, h0, w0, h0, stack, 0.0, 0.0, 0.25, 0.0, true);
            minmax = extrema(stack, null, w0, h0, true);
            draw_array(stack, null, minmax[0], minmax[1], w0, h0, true, false, "/Users/Pete/Desktop/moon2/d-stack-"+i+".png");
        }
    }
}
