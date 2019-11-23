package com.apps.talkit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.talkit.classes.PostInfo;
import com.apps.talkit.recyclers_fragments.RecyclerViewAdapterComments;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.Map;

public class ExpandedPostActivity extends BaseActivity {

    private TextView numUpvotes;
    private ImageButton upvoteButton;
    private PostInfo post;
    private Button chatButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        post = (PostInfo) i.getSerializableExtra("post");
        boolean openDialog = i.getBooleanExtra("openDialog", false);
        int theme = i.getIntExtra("theme",0);
        if(theme==1){
            setTheme(R.style.AppThemeTwo);
        }
        else if(theme==2){
            setTheme(R.style.AppThemeThree);
        }
        else{
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_expanded_post);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView title = findViewById(R.id.primary_text);
        TextView supportingText = findViewById(R.id.supporting_text);
        numUpvotes = findViewById(R.id.num_upvotes);
        upvoteButton = findViewById(R.id.action_button_1);
        FloatingActionButton floatingActionButton = findViewById(R.id.fab);
        chatButton = findViewById(R.id.action_button_3);

        title.setText(post.getPostTitle());
        supportingText.setText(post.getPostText());
        numUpvotes.setText(String.valueOf(post.getNumberOfUpvotes()));
        if (post.getUpvoted()) {
            upvoteButton.setImageResource(R.drawable.arrow_upvoted);
        } else {
            upvoteButton.setImageResource(R.drawable.arrow_normal);
        }

        upvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!post.getUpvoted()) {
                    post.setUpvoted(true);
                    post.setNumberOfUpvotes(post.getNumberOfUpvotes() + 1);
                    numUpvotes.setText(String.valueOf(post.getNumberOfUpvotes()));
                    upvoteButton.setImageResource(R.drawable.arrow_upvoted);
                } else {
                    post.setUpvoted(false);
                    post.setNumberOfUpvotes(post.getNumberOfUpvotes() - 1);
                    numUpvotes.setText(String.valueOf(post.getNumberOfUpvotes()));
                    upvoteButton.setImageResource(R.drawable.arrow_normal);
                }
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCommentDialogBox();
            }
        });

        RecyclerView recyclerView = findViewById(R.id.comments);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.Adapter mAdapter = new RecyclerViewAdapterComments(this, post.getComments());
        recyclerView.setAdapter(mAdapter);

        if (!post.getChatEnabled()) {
            chatButton.setVisibility(View.INVISIBLE);
        }

        if (openDialog) {
            showCommentDialogBox();
        }
    }

    private void showCommentDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ExpandedPostActivity.this);
        builder.setTitle("Write your comment");
        builder.setCancelable(false);

        // Set up the input
        final EditText input = new EditText(getApplicationContext());
        // Specify the type of input expected; this, for example
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("Comment", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Map<String, String> temp = Collections.emptyMap();
                if (post.getComments() != null) {
                    temp = post.getComments();
                }
                temp.put("You", input.getText().toString().trim());
                post.setComments(temp);
            }
        });
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
