import { useEffect, useState } from 'react'
import { LinearProgress, Stack, Table, TableBody, TableCell, TableHead, TableRow, Typography } from '@mui/material'
import { versionApi } from '../../../core/api/versionApi'
import type { VersionHistory } from '../../../core/types'

export const VersionHistoryPage = () => {
  const [versions, setVersions] = useState<VersionHistory[]>([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const load = async () => {
      const data = await versionApi.getVersionHistory()
      setVersions(data || [])
      setLoading(false)
    }
    load()
  }, [])

  if (loading) return <LinearProgress />

  return (
    <Stack spacing={2}>
      <Typography variant="h5" fontWeight={800}>
        Historial de versiones
      </Typography>
      <Table size="small">
        <TableHead>
          <TableRow>
            <TableCell>Versión</TableCell>
            <TableCell>Descripción</TableCell>
            <TableCell>Fecha</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {versions.map((version) => (
            <TableRow key={version.id}>
              <TableCell>{version.versionNumber}</TableCell>
              <TableCell>{version.description}</TableCell>
              <TableCell>{new Date(version.releasedAt).toLocaleDateString()}</TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </Stack>
  )
}
