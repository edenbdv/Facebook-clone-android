// Generated by view binder compiler. Do not edit!
package com.example.foobar.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.foobar.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class CommentItemBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView imgDelete;

  @NonNull
  public final ImageView imgEdit;

  @NonNull
  public final TextView textViewComment;

  private CommentItemBinding(@NonNull LinearLayout rootView, @NonNull ImageView imgDelete,
      @NonNull ImageView imgEdit, @NonNull TextView textViewComment) {
    this.rootView = rootView;
    this.imgDelete = imgDelete;
    this.imgEdit = imgEdit;
    this.textViewComment = textViewComment;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static CommentItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static CommentItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.comment_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static CommentItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.imgDelete;
      ImageView imgDelete = ViewBindings.findChildViewById(rootView, id);
      if (imgDelete == null) {
        break missingId;
      }

      id = R.id.imgEdit;
      ImageView imgEdit = ViewBindings.findChildViewById(rootView, id);
      if (imgEdit == null) {
        break missingId;
      }

      id = R.id.textViewComment;
      TextView textViewComment = ViewBindings.findChildViewById(rootView, id);
      if (textViewComment == null) {
        break missingId;
      }

      return new CommentItemBinding((LinearLayout) rootView, imgDelete, imgEdit, textViewComment);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
