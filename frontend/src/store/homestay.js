import { defineStore } from 'pinia'

export const useHomestayStore = defineStore('homestay', {
  state: () => ({
    homestayList: [],
    currentHomestay: null,
    recommendHomestays: [],
    searchParams: {
      keyword: '',
      minPrice: null,
      maxPrice: null,
      tags: '',
      page: 1,
      size: 10
    }
  }),
  
  getters: {
    totalHomestays: (state) => state.homestayList.length
  },
  
  actions: {
    setHomestayList(list) {
      this.homestayList = list
    },
    
    setCurrentHomestay(homestay) {
      this.currentHomestay = homestay
    },
    
    setRecommendHomestays(list) {
      this.recommendHomestays = list
    },
    
    setSearchParams(params) {
      this.searchParams = { ...this.searchParams, ...params }
    },
    
    resetSearchParams() {
      this.searchParams = {
        keyword: '',
        minPrice: null,
        maxPrice: null,
        tags: '',
        page: 1,
        size: 10
      }
    }
  }
})
