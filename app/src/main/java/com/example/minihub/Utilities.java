package com.example.minihub;


import com.example.minihub.domain.Payload;
import com.example.minihub.network.FeedAsyncTask;

import static com.example.minihub.network.FeedAsyncTask.ERROR_INCORRECT_DATA;
import static com.example.minihub.network.FeedAsyncTask.ERROR_NO_NETWORK;

public class Utilities {
    public static int getErrorMessage(@FeedAsyncTask.Error int errorCode) {
        switch (errorCode) {
            case ERROR_INCORRECT_DATA: return R.string.error_incorrect_data;
            case ERROR_NO_NETWORK: return R.string.error_no_network;
            default: return R.string.unknown_error;
        }
    }
}
