package com.milovan.materialdrawerdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.crossfader.view.CrossFadeSlidingPaneLayout;
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_FRAGMENT_FIRST = FirstFragment.class.getCanonicalName();
    private static final String TAG_FRAGMENT_SECOND = SecondFragment.class.getCanonicalName();

    private static final int DRAWER_ITEM__OPTION_ONE = 1;
    private static final int DRAWER_ITEM__OPTION_TWO = 2;

    private Drawer drawer;
    private Crossfader crossfader;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        setUpDrawerMenu(savedInstanceState);
        showFirstFragment();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState = drawer.saveInstanceState(outState);
        outState = crossfader.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    // ------------------------------------------------------------------------------------ INTERNAL

    @SuppressWarnings("ConstantConditions")
    private void setUpDrawerMenu(@Nullable Bundle savedInstanceState) {
        DrawerBuilder drawerBuilder = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .withTranslucentNavigationBar(false)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                                .withIdentifier(DRAWER_ITEM__OPTION_ONE)
                                .withName("Option 1")
                                .withIcon(FontAwesome.Icon.faw_user),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem()
                                .withIdentifier(DRAWER_ITEM__OPTION_TWO)
                                .withName("Option 2")
                                .withIcon(FontAwesome.Icon.faw_user_friends),
                        new DividerDrawerItem()
                )
                .withOnDrawerItemClickListener(
                        (view, position, drawerItem) ->
                                onDrawerItemClicked(drawerItem.getIdentifier())
                )
                .withSavedInstance(savedInstanceState)
                .withGenerateMiniDrawer(true)
                .withSelectedItem(DRAWER_ITEM__OPTION_ONE);

        drawer = drawerBuilder.buildView();

        MiniDrawer miniDrawer = drawer.getMiniDrawer().withInnerShadow(true);
        View miniView = miniDrawer.build(this);
        miniView.setBackgroundColor(ContextCompat.getColor(this, R.color.primary));

        Resources res = getResources();
        crossfader = new Crossfader<CrossFadeSlidingPaneLayout>()
                .withContent(findViewById(R.id.main__fragment_container))
                .withCanSlide(true)
                .withResizeContentPanel(true)
                .withFirst(drawer.getSlider(), res.getDimensionPixelSize(R.dimen.main__drawer__width))
                .withSecond(miniView, res.getDimensionPixelSize(R.dimen.main__mini_drawer__width))
                .withSavedInstance(savedInstanceState)
                .build();

        miniDrawer.withCrossFader(new CrossfadeWrapper(crossfader));
        crossfader.getCrossFadeSlidingPaneLayout().setShadowResourceLeft(R.drawable.material_drawer_shadow_left);
    }

    private boolean onDrawerItemClicked(long identifier) {
        switch ((int) identifier) {
            case DRAWER_ITEM__OPTION_ONE:
                showFirstFragment();
                break;
            case DRAWER_ITEM__OPTION_TWO:
                showSecondFragment();
                break;
            default:
                break;
        }
        return true;
    }

    private void showFirstFragment() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.main__fragment_container);
        if (fragment instanceof FirstFragment) return;

        fragmentManager.beginTransaction()
                .replace(R.id.main__fragment_container,
                        FirstFragment.newInstance(),
                        TAG_FRAGMENT_FIRST)
                .commitAllowingStateLoss();
    }

    private void showSecondFragment() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.main__fragment_container);
        if (fragment instanceof SecondFragment) return;

        fragmentManager.beginTransaction()
                .replace(R.id.main__fragment_container,
                        SecondFragment.newInstance(),
                        TAG_FRAGMENT_SECOND)
                .commitAllowingStateLoss();
    }
}
