package com.example.android.politicalpreparedness.representative.adapter

import android.widget.*
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.VoterInfo

@BindingAdapter("profileImage")
fun fetchImage(view: ImageView, src: String?) {
    src?.let {
        val uri = src.toUri().buildUpon().scheme("https").build()
        //Glide call to load image and circle crop - user ic_profile as a placeholder and for errors.
        Glide.with(view)
            .load(uri)
            .apply ( RequestOptions()
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .circleCrop()
            )
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}

@BindingAdapter("stateValue")
fun Spinner.setNewValue(value: String?) {
    val adapter = toTypedAdapter<String>(this.adapter as ArrayAdapter<*>)
    val position = when (adapter.getItem(0)) {
        is String -> adapter.getPosition(value)
        else -> this.selectedItemPosition
    }
    if (position >= 0) {
        setSelection(position)
    }
}

inline fun <reified T> toTypedAdapter(adapter: ArrayAdapter<*>): ArrayAdapter<T>{
    return adapter as ArrayAdapter<T>
}

//Election
@BindingAdapter("electionInfoTitle")
fun bindElectionInfoTitleText(view: TextView, voterInfo: VoterInfo?) {
    voterInfo?.run {
        view.text = view.resources.getString(R.string.election_info_text_with_var, state)
    }
}

@BindingAdapter("followButtonText")
fun bindFollowButtonText(button: Button, isElectionSaved: Boolean?) {
    if(isElectionSaved != null) {
        if (isElectionSaved) {
            button.text = button.resources.getString(R.string.unfollow_button)
        } else {
            button.text = button.resources.getString(R.string.follow_button)
        }
    } else {
        button.text = ""
    }
}