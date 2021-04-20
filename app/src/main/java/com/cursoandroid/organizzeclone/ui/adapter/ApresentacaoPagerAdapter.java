package com.cursoandroid.organizzeclone.ui.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.cursoandroid.organizzeclone.ui.fragment.Apresentacao2Fragment;
import com.cursoandroid.organizzeclone.ui.fragment.Apresentacao3Fragment;
import com.cursoandroid.organizzeclone.ui.fragment.Apresentacao4Fragment;
import com.cursoandroid.organizzeclone.ui.fragment.ApresentacaoFragment;

import java.util.ArrayList;
import java.util.List;

public class ApresentacaoPagerAdapter extends FragmentStateAdapter {
    private Listenner listenner;
    private List<Fragment> fragments;
    public ApresentacaoPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Listenner listenner) {
        super(fragmentManager, lifecycle);
        fragments = new ArrayList<>();
        fragments.add(new ApresentacaoFragment());
        fragments.add(new Apresentacao2Fragment());
        fragments.add(new Apresentacao3Fragment());
        fragments.add(new Apresentacao4Fragment());
        fragments.add(new Apresentacao4Fragment());
        this.listenner = listenner;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position == 4){
            listenner.lastFragment();
        }
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size() ;
    }
    public  interface Listenner {
        public void lastFragment();

    }
}

