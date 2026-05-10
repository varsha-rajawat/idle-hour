// ================================================================
// types/index.ts — TypeScript interfaces that mirror the backend DTOs.
// These are the source of truth for what the API returns.
// ================================================================

export type Role = 'USER' | 'ADMIN'
export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE'
export type TaskPriority = 'LOW' | 'MEDIUM' | 'HIGH'

// ── Auth ──────────────────────────────────────────────────────
export interface AuthResponse {
  accessToken: string
  refreshToken: string
  userId: string
  name: string
  email: string
  role: Role
}

// ── User ──────────────────────────────────────────────────────
export interface User {
  id: string
  name: string
  email: string
  role: Role
  createdAt: string
}

// ── Task ──────────────────────────────────────────────────────
export interface Task {
  id: string
  title: string
  description: string | null
  status: TaskStatus
  priority: TaskPriority
  createdById: string | null
  createdByName: string | null
  assignedToId: string | null
  assignedToName: string | null
  dueDate: string | null    // ISO date string "2025-01-15"
  createdAt: string
  updatedAt: string
}

export interface CreateTaskRequest {
  title: string
  description?: string
  priority?: TaskPriority
  assigneeId?: string
  dueDate?: string
}

export interface UpdateTaskRequest {
  title?: string
  description?: string
  status?: TaskStatus
  priority?: TaskPriority
  assigneeId?: string
  dueDate?: string
}

// ── Pagination ────────────────────────────────────────────────
// Spring's Page<T> response shape
export interface Page<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number      // current page (0-based)
  size: number
  last: boolean
  first: boolean
}

// ── Dashboard ─────────────────────────────────────────────────
export interface DashboardStats {
  total: number
  myTasks: number
  overdue: number
  completedToday: number
}

// ── Auth slice state ──────────────────────────────────────────
export interface AuthState {
  user: Pick<AuthResponse, 'userId' | 'name' | 'email' | 'role'> | null
  accessToken: string | null
  refreshToken: string | null
  loading: boolean
  error: string | null
}

// ── Tasks slice state ─────────────────────────────────────────
export interface TaskFilters {
  status?: TaskStatus
  priority?: TaskPriority
  assigneeId?: string
  search?: string
  page: number
  size: number
  sortBy: string
  sortDir: 'asc' | 'desc'
}

export interface TasksState {
  tasks: Task[]
  totalElements: number
  totalPages: number
  currentPage: number
  filters: TaskFilters
  loading: boolean
  error: string | null
}
