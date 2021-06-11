package com.ybond.data_network.websites

import com.ybond.core_entities.models.SongsWebsite

interface WebsiteDataSourcesHolder {

    fun getDataSource(website: SongsWebsite) : SongsWebsiteDataSource

    companion object {
        operator fun invoke(): WebsiteDataSourcesHolder = WebsiteDataSourcesHolderImpl()
    }

}