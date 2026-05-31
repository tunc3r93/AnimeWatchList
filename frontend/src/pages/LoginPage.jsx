import React, { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function LoginPage() {
  const navigate = useNavigate()
  const { login } = useAuth()
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError('')

    try {
      await login(email, password)
      navigate('/')
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-slate-900 via-slate-800 to-black flex items-center justify-center px-4">
      <div className="w-full max-w-md">
        {/* Logo Section */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-16 h-16 bg-gradient-to-br from-orange-500 to-red-600 rounded-2xl mb-4 shadow-lg">
            <span className="text-white font-bold text-3xl">A</span>
          </div>
          <h1 className="text-4xl font-bold bg-gradient-to-r from-orange-500 to-red-500 bg-clip-text text-transparent mb-2">
            AnimeWatch
          </h1>
          <p className="text-gray-400">Deine persönliche Anime-Sammlung</p>
        </div>

        {/* Login Card */}
        <div className="bg-slate-800 rounded-2xl border border-orange-500/20 p-8 shadow-2xl">
          <h2 className="text-2xl font-bold text-white mb-6">Willkommen zurück</h2>

          {error && (
            <div className="mb-6 p-4 rounded-lg bg-red-500/10 border border-red-500/50 text-red-300 flex items-start gap-3">
              <span>❌</span>
              <div>{error}</div>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-300 mb-2">E-Mail</label>
              <input
                type="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
                placeholder="deine@email.com"
                className="w-full px-4 py-3 rounded-lg bg-slate-700 border border-orange-500/30 text-white placeholder-gray-500 focus:outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-500/50 transition-all"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-300 mb-2">Passwort</label>
              <input
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
                placeholder="••••••••"
                className="w-full px-4 py-3 rounded-lg bg-slate-700 border border-orange-500/30 text-white placeholder-gray-500 focus:outline-none focus:border-orange-500 focus:ring-2 focus:ring-orange-500/50 transition-all"
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-gradient-to-r from-orange-500 to-red-600 hover:from-orange-600 hover:to-red-700 text-white font-bold py-3 rounded-lg transition-all disabled:opacity-50 disabled:cursor-not-allowed mt-6"
            >
              {loading ? '⏳ Anmelden...' : '🚀 Anmelden'}
            </button>
          </form>

          <p className="text-center text-gray-400 mt-6">
            Noch kein Account?{' '}
            <Link to="/register" className="text-orange-400 hover:text-orange-300 font-medium transition-colors">
              Jetzt registrieren
            </Link>
          </p>
        </div>

        {/* Demo Credentials */}
        <div className="mt-8 bg-slate-700/50 rounded-xl border border-orange-500/20 p-6">
          <p className="text-sm text-gray-400 mb-3">
            <span className="text-orange-400 font-bold">💡 Demo-Zugangsdaten:</span>
          </p>
          <div className="space-y-2 text-sm font-mono text-gray-300 bg-black/50 rounded-lg p-3">
            <p>📧 <span className="text-orange-400">admin@example.com</span></p>
            <p>🔐 <span className="text-orange-400">admin123</span></p>
          </div>
          <p className="text-xs text-gray-500 mt-3">
            Dies ist ein Admin-Account. Verwende diesen zum Testen aller Funktionen.
          </p>
        </div>
      </div>
    </div>
  )
}
