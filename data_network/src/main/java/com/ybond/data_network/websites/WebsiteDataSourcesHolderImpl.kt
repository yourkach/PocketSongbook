package com.ybond.data_network.websites

import com.ybond.core_entities.models.SongsWebsite

internal class WebsiteDataSourcesHolderImpl : WebsiteDataSourcesHolder {

    private val amdmSource by lazy { AmdmWebsiteDataSource() }
    private val myChordsSource by lazy { MychordsWebsiteDataSource() }

    override fun getDataSource(website: SongsWebsite): SongsWebsiteDataSource {
        return when(website) {
            SongsWebsite.AmDm -> amdmSource
            SongsWebsite.MyChords -> myChordsSource
        }
    }

}