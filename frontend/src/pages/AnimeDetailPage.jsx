import React, { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { api } from '../api/api'

export default function AnimeDetailPage() {
  const { id } = useParams()
  const navigate = useNavigate()
  const [anime, setAnime] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [ratingForm, setRatingForm] = useState({ score: 5, comment: '' })

  useEffect(() => {
    loadAnime()
  }, [id])

  const loadAnime = async () => {
    try {
      const data = await api.getAnimeById(id)
      setAnime(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const handleAddToWatchlist = async () => {
    try {
      await api.addToWatchlist(id, 'PLAN_TO_WATCH')
      loadAnime()
    } catch (err) {
      setError(err.message)
    }
  }

  const handleRatingSubmit = async (e) => {
    e.preventDefault()
    try {
      // This would call rating API in full implementation
      loadAnime()
    } catch (err) {
      setError(err.message)
    }
  }

  if (loading) return <div style={{ padding: '20px' }}>Loading...</div>
  if (error) return <div style={{ padding: '20px', color: 'red' }}>{error}</div>
  if (!anime) return <div style={{ padding: '20px' }}>Anime not found</div>

  return (
    <div style={{ padding: '20px', maxWidth: '1200px', margin: '0 auto' }}>
      <button onClick={() => navigate(-1)} style={{ marginBottom: '20px' }}>← Back</button>

      <div style={{ display: 'grid', gridTemplateColumns: '300px 1fr', gap: '30px' }}>
        <div>
          <img src={anime.coverUrl} alt={anime.title} style={{ width: '100%', borderRadius: '8px' }} />
          <button onClick={handleAddToWatchlist} style={{ width: '100%', padding: '10px', marginTop: '10px' }}>
            Add to Watchlist
          </button>
        </div>

        <div>
          <h1>{anime.title}</h1>
          <p><strong>Status:</strong> {anime.status}</p>
          <p><strong>Episodes:</strong> {anime.episodeCount}</p>
          <p><strong>Release Year:</strong> {anime.releaseYear}</p>
          <p><strong>Genre:</strong> {anime.genre}</p>
          <p><strong>Rating:</strong> {anime.avgRating || 'Not rated'}/10</p>
          <p><strong>Description:</strong></p>
          <p>{anime.description}</p>

          <h3 style={{ marginTop: '30px' }}>User Ratings ({anime.ratingCount})</h3>
          {anime.ratings && anime.ratings.map(r => (
            <div key={r.id} style={{ marginTop: '10px', padding: '10px', backgroundColor: '#f5f5f5', borderRadius: '4px' }}>
              <strong>{r.user?.firstName} {r.user?.lastName}</strong> - {r.score}/10
              <p>{r.comment}</p>
            </div>
          ))}
        </div>
      </div>
    </div>
  )
}
