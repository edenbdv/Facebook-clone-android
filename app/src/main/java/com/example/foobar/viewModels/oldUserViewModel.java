//package com.example.foobar.viewModels;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.example.foobar.entities.Post_Item;
//import com.example.foobar.repositories.FeedRepository;
//
//import java.util.List;
//
//public class oldUserViewModel extends ViewModel {
//
//    private FeedRepository mRepository;
//
//    private LiveData<List<Post_Item>> posts;
//
//    public oldUserViewModel() {
//        mRepository = new FeedRepository();
//        posts = mRepository.getAll();
//    }
//
//    public LiveData<List<Post_Item>> get() {
//        return posts;
//    }
//
//    public void add(Post_Item post) {
//        mRepository.add(post);
//    }
//
//    public void delete(Post_Item post) {
//        mRepository.delete(post);
//    }
//
//    public void reload() {
//        //mRepository.reload();
//    }
//
//
//}
//
