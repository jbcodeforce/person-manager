import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import Persons from '../views/Persons.vue'
import Meetings from '../views/Meetings.vue'
import Projects from '../views/Projects.vue'

Vue.use(VueRouter)

const routes = [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
        path: '/persons',
        name: 'persons',
        component: Persons
      },
    {
      path: '/meetings',
      name: 'meetings',
      component: Meetings
    },
    {
      path: '/projects',
      name: 'projects',
      component: Projects
    }
]

const router = new VueRouter({
    routes
  })
  
export default router