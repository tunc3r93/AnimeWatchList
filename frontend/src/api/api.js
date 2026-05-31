const API_BASE = 'http://localhost:8080/api/v1'

async function request(endpoint, options = {}) {
  const token = localStorage.getItem('accessToken')
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers
  }

  if (token) {
    headers.Authorization = `Bearer ${token}`
  }

  const response = await fetch(`${API_BASE}${endpoint}`, {
    ...options,
    headers
  })

  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || `HTTP ${response.status}`)
  }

  if (response.status === 204) return null
  const contentType = response.headers.get('content-type')
  return contentType?.includes('json') ? response.json() : response.text()
}

export const api = {
  // Auth
  login: (email, password) => request('/auth/login', {
    method: 'POST',
    body: JSON.stringify({ email, password })
  }),

  register: (data) => request('/auth/register', {
    method: 'POST',
    body: JSON.stringify(data)
  }),

  // Anime
  getAnime: (page = 0, size = 20, search = '', genre = '', status = '') => {
    const params = new URLSearchParams({ page, size })
    if (search) params.append('search', search)
    if (genre) params.append('genre', genre)
    if (status) params.append('status', status)
    return request(`/anime?${params}`)
  },

  getAnimeById: (id) => request(`/anime/${id}`),

  createAnime: (data) => request('/anime', {
    method: 'POST',
    body: JSON.stringify(data)
  }),

  updateAnime: (id, data) => request(`/anime/${id}`, {
    method: 'PUT',
    body: JSON.stringify(data)
  }),

  deleteAnime: (id) => request(`/anime/${id}`, { method: 'DELETE' }),

  // Watchlist
  getWatchlist: (page = 0, size = 20, status = '') => {
    const params = new URLSearchParams({ page, size })
    if (status) params.append('status', status)
    return request(`/watchlist?${params}`)
  },

  addToWatchlist: (animeId, status) => request('/watchlist', {
    method: 'POST',
    body: JSON.stringify({ animeId, status })
  }),

  updateWatchlistStatus: (id, status) => request(`/watchlist/${id}`, {
    method: 'PUT',
    body: JSON.stringify({ status })
  }),

  removeFromWatchlist: (id) => request(`/watchlist/${id}`, { method: 'DELETE' }),

  // Ratings
  getRatingsForAnime: (animeId) => request(`/ratings/anime/${animeId}`),

  createRating: (data) => request('/ratings', {
    method: 'POST',
    body: JSON.stringify(data)
  }),

  // Admin
  getInvitations: (page = 0, size = 20) => request(`/admin/invitations?page=${page}&size=${size}`),

  createInvitation: (data) => request('/admin/invitations', {
    method: 'POST',
    body: JSON.stringify(data)
  })
}
