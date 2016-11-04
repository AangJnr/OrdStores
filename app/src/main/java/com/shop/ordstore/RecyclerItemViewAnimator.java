package com.shop.ordstore;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.shop.ordstore.UserClasses.OrdersCardAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Miroslaw Stanek on 02.12.2015.
 */
public class RecyclerItemViewAnimator extends DefaultItemAnimator {
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR = new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final OvershootInterpolator OVERSHOOT_INTERPOLATOR = new OvershootInterpolator(4);

    Map<RecyclerView.ViewHolder, AnimatorSet> likeAnimationsMap = new HashMap<>();
    Map<RecyclerView.ViewHolder, AnimatorSet> heartAnimationsMap = new HashMap<>();

    private int lastAddAnimatedItem = -2;

    @Override
    public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
        return true;
    }

    @NonNull
    @Override
    public ItemHolderInfo recordPreLayoutInformation(@NonNull RecyclerView.State state,
                                                     @NonNull RecyclerView.ViewHolder viewHolder,
                                                     int changeFlags, @NonNull List<Object> payloads) {
        if (changeFlags == FLAG_CHANGED) {
            for (Object payload : payloads) {
                if (payload instanceof String) {
                    return new FeedItemHolderInfo((String) payload);
                }
            }
        }

        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads);
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder viewHolder) {
            if (viewHolder.getLayoutPosition() > lastAddAnimatedItem) {
                lastAddAnimatedItem++;
                runEnterAnimation((OrdersCardAdapter.OrdersViewHolder) viewHolder);
                return false;
            }


        dispatchAddFinished(viewHolder);
        return false;
    }

    private void runEnterAnimation(final OrdersCardAdapter.OrdersViewHolder holder) {
        final int screenHeight = Utils.getScreenHeight(holder.itemView.getContext());
        holder.itemView.setTranslationY(screenHeight);
        holder.itemView.animate()
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator(3.f))
                .setDuration(700)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        dispatchAddFinished(holder);
                    }
                })
                .start();
    }

    @Override
    public boolean animateChange(@NonNull RecyclerView.ViewHolder oldHolder,
                                 @NonNull RecyclerView.ViewHolder newHolder,
                                 @NonNull ItemHolderInfo preInfo,
                                 @NonNull ItemHolderInfo postInfo) {
        cancelCurrentAnimationIfExists(newHolder);




        return false;
    }

    private void cancelCurrentAnimationIfExists(RecyclerView.ViewHolder item) {
        if (likeAnimationsMap.containsKey(item)) {
            likeAnimationsMap.get(item).cancel();
        }
        if (heartAnimationsMap.containsKey(item)) {
            heartAnimationsMap.get(item).cancel();
        }
    }







    @Override
    public void endAnimation(RecyclerView.ViewHolder item) {
        super.endAnimation(item);
        cancelCurrentAnimationIfExists(item);
    }

    @Override
    public void endAnimations() {
        super.endAnimations();
        for (AnimatorSet animatorSet : likeAnimationsMap.values()) {
            animatorSet.cancel();
        }
    }

    public static class FeedItemHolderInfo extends ItemHolderInfo {
        public String updateAction;

        public FeedItemHolderInfo(String updateAction) {
            this.updateAction = updateAction;
        }
    }
}