package com.bignerdranch.android.networkarchitecture;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import static org.hamcrest.CoreMatchers.equalTo;

import static org.junit.Assert.assertThat;

/**
 * Created by Chris Hare on 10/20/2015.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(sdk = 21,constants = BuildConfig.class)
public class ApplicationTest {
    @Test
    public void testRobolectricSetupWorks() {
        assertThat(1, equalTo(1));

    }
}
