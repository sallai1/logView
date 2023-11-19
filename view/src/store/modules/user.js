import { login, logout, getInfo } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import { resetRouter } from '@/router'

const getDefaultState = () => {
  return {
    token: getToken(),
    name: '',
    avatar: ''
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password } = userInfo
    const params = new URLSearchParams() // 创建对象
          params.append("loginUsername",username);
          params.append("loginPassword",password);
    return new Promise((resolve, reject) => {
      login(params).then(response => {
        console.log(response);
        if(response === "error") {
          console.log("用户名或密码错误");
          reject("用户名或密码错误");
        }
        commit('SET_TOKEN', "123456")
        setToken("123456")
        resolve()
      }).catch(error => {
        // resolve();
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      commit('SET_NAME', "admin")
      commit('SET_AVATAR', "../../assets/head_img/girl.jpeg")
      resolve()
      // getInfo(state.token).then(response => {
      //   // const { data } = response

      //   // if (!data) {
      //   //   return reject('Verification failed, please Login again.')
      //   // }

      //   const { name, avatar } = data

        
      // }).catch(error => {
      //   reject(error)
      // })
    })
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        removeToken() // must remove  token  first
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

