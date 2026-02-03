const state = {
  visitedViews: [],
  cachedViews: []
}

const mutations = {
  ADD_VISITED_VIEW: (state, view) => {
    if (state.visitedViews.some(v => v.path === view.path)) return
    state.visitedViews.push({
      name: view.name,
      path: view.path,
      fullPath: view.fullPath || view.path,
      title: view.meta && view.meta.title ? view.meta.title : 'no-name',
      meta: { ...view.meta }
    })
  },
  ADD_CACHED_VIEW: (state, view) => {
    const name = view.name
    if (!name) return
    if (state.cachedViews.includes(name)) return
    state.cachedViews.push(name)
  },
  DEL_VISITED_VIEW: (state, view) => {
    state.visitedViews = state.visitedViews.filter(v => v.path !== view.path)
  },
  DEL_CACHED_VIEW: (state, view) => {
    state.cachedViews = state.cachedViews.filter(n => n !== view.name)
  },
  DEL_OTHERS_VISITED_VIEWS: (state, view) => {
    state.visitedViews = state.visitedViews.filter(v => v.path === view.path)
  },
  DEL_OTHERS_CACHED_VIEWS: (state, view) => {
    state.cachedViews = state.cachedViews.filter(n => n === view.name)
  },
  DEL_ALL_VISITED_VIEWS: (state) => {
    state.visitedViews = []
  },
  DEL_ALL_CACHED_VIEWS: (state) => {
    state.cachedViews = []
  }
}

const actions = {
  addView({ dispatch }, view) {
    dispatch('addVisitedView', view)
    dispatch('addCachedView', view)
  },
  addVisitedView({ commit }, view) {
    commit('ADD_VISITED_VIEW', view)
  },
  addCachedView({ commit }, view) {
    commit('ADD_CACHED_VIEW', view)
  },
  delView({ dispatch, state }, view) {
    return new Promise(resolve => {
      dispatch('delVisitedView', view)
      dispatch('delCachedView', view)
      resolve({
        visitedViews: [...state.visitedViews],
        cachedViews: [...state.cachedViews]
      })
    })
  },
  delVisitedView({ commit }, view) {
    commit('DEL_VISITED_VIEW', view)
  },
  delCachedView({ commit }, view) {
    commit('DEL_CACHED_VIEW', view)
  },
  delOthersViews({ dispatch }, view) {
    dispatch('delOthersVisitedViews', view)
    dispatch('delOthersCachedViews', view)
  },
  delOthersVisitedViews({ commit }, view) {
    commit('DEL_OTHERS_VISITED_VIEWS', view)
  },
  delOthersCachedViews({ commit }, view) {
    commit('DEL_OTHERS_CACHED_VIEWS', view)
  },
  delAllViews({ dispatch }) {
    dispatch('delAllVisitedViews')
    dispatch('delAllCachedViews')
  },
  delAllVisitedViews({ commit }) {
    commit('DEL_ALL_VISITED_VIEWS')
  },
  delAllCachedViews({ commit }) {
    commit('DEL_ALL_CACHED_VIEWS')
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
