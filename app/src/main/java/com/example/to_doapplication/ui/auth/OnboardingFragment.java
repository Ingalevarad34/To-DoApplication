package com.example.to_doapplication.ui.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.to_doapplication.R;
import com.example.to_doapplication.databinding.FragmentOnboardingBinding;

public class OnboardingFragment extends Fragment {

    private FragmentOnboardingBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int[] images = new int[]{R.drawable.frame_161, R.drawable.frame_162, R.drawable.frame_182};
        String[] titles = new String[]{"Manage your tasks", "Create daily routine", "Organize your tasks"};
        String[] descriptions = new String[]{
                "You can easily manage all of your daily tasks in DoMe for free",
                "In Uptodo you can create your personalized routine to stay productive",
                "You can organize your daily tasks by adding your tasks into separate categories"
        };

        OnboardingPagerAdapter adapter = new OnboardingPagerAdapter(images, titles, descriptions);
        binding.viewPagerOnboarding.setAdapter(adapter);

        binding.btnOnboardingSkip.setOnClickListener(v -> navigateToStart());

        binding.btnOnboardingNext.setOnClickListener(v -> {
            int current = binding.viewPagerOnboarding.getCurrentItem();
            if (current < images.length - 1) {
                binding.viewPagerOnboarding.setCurrentItem(current + 1);
            } else {
                navigateToStart();
            }
        });

        binding.btnOnboardingBack.setOnClickListener(v -> {
            int current = binding.viewPagerOnboarding.getCurrentItem();
            if (current > 0) {
                binding.viewPagerOnboarding.setCurrentItem(current - 1);
            }
        });

        binding.viewPagerOnboarding.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == images.length - 1) {
                    binding.btnOnboardingNext.setText("GET STARTED");
                } else {
                    binding.btnOnboardingNext.setText("NEXT");
                }
                binding.btnOnboardingBack.setVisibility(position > 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });
    }

    private void navigateToStart() {
        if (getActivity() instanceof AuthActivity) {
            ((AuthActivity) getActivity()).navigateToStart();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    static class OnboardingPagerAdapter extends RecyclerView.Adapter<OnboardingPagerAdapter.ViewHolder> {
        private final int[] images;
        private final String[] titles;
        private final String[] descriptions;

        public OnboardingPagerAdapter(int[] images, String[] titles, String[] descriptions) {
            this.images = images;
            this.titles = titles;
            this.descriptions = descriptions;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_onboarding_page, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.img.setImageResource(images[position]);
            holder.tvTitle.setText(titles[position]);
            holder.tvDesc.setText(descriptions[position]);
        }

        @Override
        public int getItemCount() {
            return images.length;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img;
            TextView tvTitle, tvDesc;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img_onboarding);
                tvTitle = itemView.findViewById(R.id.tv_onboarding_title);
                tvDesc = itemView.findViewById(R.id.tv_onboarding_description);
            }
        }
    }
}
