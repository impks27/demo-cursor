import { useState, useEffect } from 'react'
import './ProfileForm.css'

const ProfileForm = ({ profile, onSubmit, onCancel }) => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    bio: '',
    avatar_url: '',
    phone: '',
    location: '',
    website: ''
  })
  const [errors, setErrors] = useState({})
  const [isSubmitting, setIsSubmitting] = useState(false)

  useEffect(() => {
    if (profile) {
      setFormData({
        name: profile.name || '',
        email: profile.email || '',
        bio: profile.bio || '',
        avatar_url: profile.avatar_url || '',
        phone: profile.phone || '',
        location: profile.location || '',
        website: profile.website || ''
      })
    }
  }, [profile])

  const validate = () => {
    const newErrors = {}

    // Name validation
    if (!formData.name.trim()) {
      newErrors.name = 'Name is required'
    } else if (formData.name.trim().length < 2) {
      newErrors.name = 'Name must be at least 2 characters'
    } else if (formData.name.length > 100) {
      newErrors.name = 'Name must be less than 100 characters'
    }

    // Email validation
    if (!formData.email.trim()) {
      newErrors.email = 'Email is required'
    } else {
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
      if (!emailRegex.test(formData.email)) {
        newErrors.email = 'Invalid email format'
      }
    }

    // Bio validation
    if (formData.bio && formData.bio.length > 1000) {
      newErrors.bio = 'Bio must be less than 1000 characters'
    }

    // Phone validation
    if (formData.phone) {
      const digitsOnly = formData.phone.replace(/\D/g, '')
      if (digitsOnly.length < 10 || digitsOnly.length > 15) {
        newErrors.phone = 'Phone number must be between 10 and 15 digits'
      }
    }

    // Website validation
    if (formData.website) {
      if (!formData.website.startsWith('http://') && !formData.website.startsWith('https://')) {
        newErrors.website = 'Website must start with http:// or https://'
      }
    }

    // Location validation
    if (formData.location && formData.location.length > 100) {
      newErrors.location = 'Location must be less than 100 characters'
    }

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: value
    }))
    // Clear error when user starts typing
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }))
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    if (!validate()) {
      return
    }

    setIsSubmitting(true)
    try {
      const url = profile 
        ? `http://localhost:8080/api/profiles/${profile.id}`
        : 'http://localhost:8080/api/profiles'
      
      const method = profile ? 'PUT' : 'POST'
      
      const response = await fetch(url, {
        method,
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(formData)
      })

      if (!response.ok) {
        const errorData = await response.json()
        if (errorData.detail) {
          if (typeof errorData.detail === 'string') {
            alert(errorData.detail)
          } else if (Array.isArray(errorData.detail)) {
            const errorMessages = errorData.detail.map(err => err.msg || err).join(', ')
            alert(errorMessages)
          } else {
            setErrors(errorData.detail)
          }
        }
        return
      }

      onSubmit()
    } catch (error) {
      console.error('Error submitting form:', error)
      alert('Error submitting form. Please try again.')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <div className="form-overlay">
      <div className="form-container">
        <h2>{profile ? 'Edit Profile' : 'Create New Profile'}</h2>
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label htmlFor="name">
              Name <span className="required">*</span>
            </label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              className={errors.name ? 'error' : ''}
            />
            {errors.name && <span className="error-message">{errors.name}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="email">
              Email <span className="required">*</span>
            </label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              className={errors.email ? 'error' : ''}
            />
            {errors.email && <span className="error-message">{errors.email}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="bio">Bio</label>
            <textarea
              id="bio"
              name="bio"
              value={formData.bio}
              onChange={handleChange}
              rows="4"
              className={errors.bio ? 'error' : ''}
            />
            {errors.bio && <span className="error-message">{errors.bio}</span>}
            <div className="char-count">
              {formData.bio.length}/1000
            </div>
          </div>

          <div className="form-group">
            <label htmlFor="avatar_url">Avatar URL</label>
            <input
              type="url"
              id="avatar_url"
              name="avatar_url"
              value={formData.avatar_url}
              onChange={handleChange}
              placeholder="https://example.com/avatar.jpg"
              className={errors.avatar_url ? 'error' : ''}
            />
            {errors.avatar_url && <span className="error-message">{errors.avatar_url}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="phone">Phone</label>
            <input
              type="tel"
              id="phone"
              name="phone"
              value={formData.phone}
              onChange={handleChange}
              className={errors.phone ? 'error' : ''}
            />
            {errors.phone && <span className="error-message">{errors.phone}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="location">Location</label>
            <input
              type="text"
              id="location"
              name="location"
              value={formData.location}
              onChange={handleChange}
              className={errors.location ? 'error' : ''}
            />
            {errors.location && <span className="error-message">{errors.location}</span>}
          </div>

          <div className="form-group">
            <label htmlFor="website">Website</label>
            <input
              type="url"
              id="website"
              name="website"
              value={formData.website}
              onChange={handleChange}
              placeholder="https://example.com"
              className={errors.website ? 'error' : ''}
            />
            {errors.website && <span className="error-message">{errors.website}</span>}
          </div>

          <div className="form-actions">
            <button
              type="button"
              onClick={onCancel}
              className="btn btn-secondary"
              disabled={isSubmitting}
            >
              Cancel
            </button>
            <button
              type="submit"
              className="btn btn-primary"
              disabled={isSubmitting}
            >
              {isSubmitting ? 'Saving...' : (profile ? 'Update' : 'Create')}
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

export default ProfileForm

