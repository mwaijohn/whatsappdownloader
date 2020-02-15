package admob;

import android.app.Activity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class Admob {

   public static String interstitial = "ca-app-pub-6460950649539083/5552847497";
    //public static String interstitial = "ca-app-pub-3940256099942544/1033173712";

    public static void banner(AdView adView){
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("335AB1B488DA376C259B9DC3E9B56BBE").build();
        adView.loadAd(adRequest);
    }

    public static  void InterstitialCall(Activity activity,String id){
        final InterstitialAd interstitialAd = new InterstitialAd(activity);
        interstitialAd.setAdUnitId(id);
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                interstitialAd.loadAd(new AdRequest.Builder().addTestDevice("335AB1B488DA376C259B9DC3E9B56BBE").build());
            }
            @Override
            public void onAdLoaded() {
                interstitialAd.show();
            }
        });
        return;
    }

    //implimenet onload
    public static  InterstitialAd InterstitialCallLoad(Activity activity,String id){
        InterstitialAd interstitialAd = new InterstitialAd(activity);
        interstitialAd.setAdUnitId(interstitial);
        interstitialAd.loadAd(new AdRequest.Builder().addTestDevice("335AB1B488DA376C259B9DC3E9B56BBE").build());
        return interstitialAd;
    }

}
