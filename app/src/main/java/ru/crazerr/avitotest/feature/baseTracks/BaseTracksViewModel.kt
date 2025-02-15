package ru.crazerr.avitotest.feature.baseTracks

import androidx.lifecycle.viewModelScope
import ru.crazerr.avitotest.domain.model.PreviewTrack
import ru.crazerr.avitotest.utils.presentation.MviViewModel
import ru.crazerr.avitotest.utils.presentation.throttleLatest

abstract class BaseTracksViewModel :
    MviViewModel<BaseTracksState, BaseTracksViewAction>(InitialBaseTracksState) {

    protected val searchQueryThrottledLambda = throttleLatest(
        coroutineScope = viewModelScope,
        destinationFunction = ::getTracks
    )

    override fun handleAction(action: BaseTracksViewAction) {
        when (action) {
            is BaseTracksViewAction.ClickTrack -> onClickTrack(previewTrack = action.previewTrack)
            is BaseTracksViewAction.UpdateSearchQuery -> onUpdateSearchQuery(search = action.search)
            BaseTracksViewAction.ClearSearchQuery -> onClearSearchQuery()
        }
    }

    protected abstract fun getTracks()

    protected abstract fun onClickTrack(previewTrack: PreviewTrack)

    protected abstract fun onUpdateSearchQuery(search: String)

    protected abstract fun onClearSearchQuery()
}