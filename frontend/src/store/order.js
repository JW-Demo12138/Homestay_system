import { defineStore } from 'pinia'

export const useOrderStore = defineStore('order', {
  state: () => ({
    userOrders: [],
    landlordOrders: [],
    currentOrder: null
  }),
  
  getters: {
    pendingOrders: (state) => state.userOrders.filter(order => order.status === '待支付'),
    paidOrders: (state) => state.userOrders.filter(order => order.status === '已支付'),
    completedOrders: (state) => state.userOrders.filter(order => order.status === '已完成')
  },
  
  actions: {
    setUserOrders(orders) {
      this.userOrders = orders
    },
    
    setLandlordOrders(orders) {
      this.landlordOrders = orders
    },
    
    setCurrentOrder(order) {
      this.currentOrder = order
    },
    
    addOrder(order) {
      this.userOrders.unshift(order)
    },
    
    updateOrder(order) {
      const index = this.userOrders.findIndex(o => o.id === order.id)
      if (index !== -1) {
        this.userOrders[index] = order
      }
      
      const landlordIndex = this.landlordOrders.findIndex(o => o.id === order.id)
      if (landlordIndex !== -1) {
        this.landlordOrders[landlordIndex] = order
      }
    }
  }
})
