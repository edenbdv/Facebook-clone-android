// Generated by view binder compiler. Do not edit!
package com.example.foobar.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.foobar.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityMainBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final Button btnCreateAccount;

  @NonNull
  public final Button btnLogin;

  @NonNull
  public final EditText editTextText;

  @NonNull
  public final EditText editTextTextPassword;

  @NonNull
  public final ImageView imageView;

  @NonNull
  public final LinearLayout linearLayout;

  @NonNull
  public final LinearLayout linearLayout2;

  @NonNull
  public final LinearLayout linearLayoutSignup;

  private ActivityMainBinding(@NonNull ConstraintLayout rootView, @NonNull Button btnCreateAccount,
      @NonNull Button btnLogin, @NonNull EditText editTextText,
      @NonNull EditText editTextTextPassword, @NonNull ImageView imageView,
      @NonNull LinearLayout linearLayout, @NonNull LinearLayout linearLayout2,
      @NonNull LinearLayout linearLayoutSignup) {
    this.rootView = rootView;
    this.btnCreateAccount = btnCreateAccount;
    this.btnLogin = btnLogin;
    this.editTextText = editTextText;
    this.editTextTextPassword = editTextTextPassword;
    this.imageView = imageView;
    this.linearLayout = linearLayout;
    this.linearLayout2 = linearLayout2;
    this.linearLayoutSignup = linearLayoutSignup;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityMainBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_main, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityMainBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnCreateAccount;
      Button btnCreateAccount = ViewBindings.findChildViewById(rootView, id);
      if (btnCreateAccount == null) {
        break missingId;
      }

      id = R.id.btnLogin;
      Button btnLogin = ViewBindings.findChildViewById(rootView, id);
      if (btnLogin == null) {
        break missingId;
      }

      id = R.id.editTextText;
      EditText editTextText = ViewBindings.findChildViewById(rootView, id);
      if (editTextText == null) {
        break missingId;
      }

      id = R.id.editTextTextPassword;
      EditText editTextTextPassword = ViewBindings.findChildViewById(rootView, id);
      if (editTextTextPassword == null) {
        break missingId;
      }

      id = R.id.imageView;
      ImageView imageView = ViewBindings.findChildViewById(rootView, id);
      if (imageView == null) {
        break missingId;
      }

      id = R.id.linearLayout;
      LinearLayout linearLayout = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout == null) {
        break missingId;
      }

      id = R.id.linearLayout2;
      LinearLayout linearLayout2 = ViewBindings.findChildViewById(rootView, id);
      if (linearLayout2 == null) {
        break missingId;
      }

      id = R.id.linearLayoutSignup;
      LinearLayout linearLayoutSignup = ViewBindings.findChildViewById(rootView, id);
      if (linearLayoutSignup == null) {
        break missingId;
      }

      return new ActivityMainBinding((ConstraintLayout) rootView, btnCreateAccount, btnLogin,
          editTextText, editTextTextPassword, imageView, linearLayout, linearLayout2,
          linearLayoutSignup);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
