package com.example.griflibrary;

import android.widget.Filter;


public abstract class FilterEdik {

    public FilterEdik() {
        throw new RuntimeException("Stub!");
    }

    public final void filter(CharSequence constraint,String categoryFilter,String authorFilter,String yearFilter,String volumeFilter,String subctractionFilter) {
        throw new RuntimeException("Stub!");
    }

    public final void filter(CharSequence constraint, Filter.FilterListener listener) {
        throw new RuntimeException("Stub!");
    }

    protected abstract FilterEdik.FilterResults performFiltering(CharSequence var1,String categoryFilter,String authorFilter,String yearFilter,String volumeFilter,String subctractionFilter);

    protected abstract void publishResults(CharSequence var1, FilterEdik.FilterResults var2);

    public CharSequence convertResultToString(Object resultValue) {
        throw new RuntimeException("Stub!");
    }

    protected static class FilterResults {
        public int count;
        public Object values;

        public FilterResults() {
            throw new RuntimeException("Stub!");
        }
    }

    public interface FilterListener {
        void onFilterComplete(int var1);
    }
}
