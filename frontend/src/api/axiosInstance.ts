import axios from 'axios'
import type { AuthResponse } from '../types'

let _accessToken: string | null = null
let _refreshToken: string | null = null
let _onTokenRefreshed: ((tokens: AuthResponse) => void) | null = null
let _onLogout: (() => void) | null = null

export function configureAxiosAuth(
    getAccessToken: () => string | null,
    getRefreshToken: () => string | null,
    onTokenRefreshed: (tokens: AuthResponse) => void,
    onLogout: () => void
) {
    _onTokenRefreshed = onTokenRefreshed
    _onLogout = onLogout

    instance.interceptors.request.use((config) => {
        const token = getAccessToken()
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`
        }
        return config
    })
}

export function setTokens(access: string | null, refresh: string | null) {
    _accessToken = access
    _refreshToken = refresh
}

const instance = axios.create({
    baseURL: '/api',
    headers: {
        'Content-Type': 'application/json',
    },
    timeout: 10000,
})

let isRefreshing = false
let failedQueue: Array<{ resolve: (value: unknown) => void; reject: (reason?: unknown) => void}> = []

const processQueue = (error: unknown, token: string | null = null) => {
    failedQueue.forEach(({resolve, reject}) => {
        if(error){
            reject(error)
        } else {
            resolve(token)
        }
    })
    failedQueue = []
}


