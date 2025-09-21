# GitHub Secrets Configuration

This document describes the required GitHub secrets for the CI/CD pipeline to function properly.

## Required Secrets

### 1. SLACK_WEBHOOK_URL
- **Description**: Slack webhook URL for deployment notifications
- **Required for**: Deployment notifications, security alerts
- **How to get**: 
  1. Go to your Slack workspace
  2. Create a new app or use existing app
  3. Enable Incoming Webhooks
  4. Create a webhook for your desired channel
  5. Copy the webhook URL

### 2. SONAR_TOKEN
- **Description**: SonarQube authentication token for code quality analysis
- **Required for**: Code quality checks
- **How to get**:
  1. Go to your SonarQube instance
  2. Log in and go to User → My Account → Security
  3. Generate a new token
  4. Copy the token

### 3. POSTGRES_PASSWORD
- **Description**: PostgreSQL database password
- **Required for**: Database operations in CI/CD
- **How to set**: Use a strong, secure password

### 4. JWT_SECRET
- **Description**: JWT signing secret key
- **Required for**: Authentication in tests
- **How to set**: Generate a strong secret (at least 256 bits)

## Optional Secrets

### 5. DOCKER_USERNAME
- **Description**: Docker Hub or container registry username
- **Required for**: Pushing Docker images
- **How to get**: Create account on Docker Hub or your container registry

### 6. DOCKER_PASSWORD
- **Description**: Docker Hub or container registry password
- **Required for**: Pushing Docker images
- **How to get**: Use your Docker Hub or container registry password

## How to Add Secrets

1. Go to your GitHub repository
2. Click on **Settings** tab
3. In the left sidebar, click **Secrets and variables** → **Actions**
4. Click **New repository secret**
5. Enter the secret name and value
6. Click **Add secret**

## Environment-Specific Secrets

### Staging Environment
- `STAGING_DB_PASSWORD`
- `STAGING_REDIS_PASSWORD`
- `STAGING_JWT_SECRET`

### Production Environment
- `PROD_DB_PASSWORD`
- `PROD_REDIS_PASSWORD`
- `PROD_JWT_SECRET`
- `PROD_SLACK_WEBHOOK_URL`

## Security Best Practices

1. **Use Strong Passwords**: Generate strong, unique passwords for each secret
2. **Rotate Regularly**: Change secrets periodically
3. **Limit Access**: Only give access to necessary team members
4. **Use Environment Variables**: Use environment-specific secrets
5. **Monitor Usage**: Regularly check secret usage in logs

## Testing Secrets

To test if secrets are properly configured:

1. **Check Workflow Logs**: Look for secret-related errors in CI/CD logs
2. **Test Notifications**: Verify Slack notifications work
3. **Test Quality Checks**: Ensure SonarQube analysis runs
4. **Test Deployments**: Verify deployment processes work

## Troubleshooting

### Common Issues

1. **Secret Not Found**: Ensure secret name matches exactly (case-sensitive)
2. **Invalid Secret**: Verify secret value is correct
3. **Permission Issues**: Check if user has access to repository secrets
4. **Environment Issues**: Ensure secrets are set for correct environment

### Debug Commands

```bash
# Check if secrets are accessible (in workflow)
echo "Testing secret access..."
if [ -n "$SLACK_WEBHOOK_URL" ]; then
  echo "Slack webhook configured"
else
  echo "Slack webhook not configured"
fi
```

## Secret Management Tools

### Recommended Tools
1. **HashiCorp Vault**: Enterprise secret management
2. **AWS Secrets Manager**: Cloud-based secret management
3. **Azure Key Vault**: Microsoft's secret management service
4. **Google Secret Manager**: Google Cloud secret management

### GitHub Actions Integration
```yaml
- name: Get secrets from Vault
  uses: hashicorp/vault-action@v2
  with:
    url: ${{ secrets.VAULT_URL }}
    token: ${{ secrets.VAULT_TOKEN }}
    secrets: |
      secret/data/myapp database_password | DB_PASSWORD
      secret/data/myapp api_key | API_KEY
```

## Compliance and Auditing

1. **Audit Logs**: GitHub provides audit logs for secret access
2. **Access Control**: Use GitHub's access control features
3. **Monitoring**: Set up alerts for secret usage
4. **Documentation**: Keep documentation of all secrets and their purposes

## Emergency Procedures

### If Secrets are Compromised
1. **Immediately rotate** the compromised secret
2. **Update all environments** with new secret
3. **Review access logs** for unauthorized usage
4. **Notify security team** if necessary

### If Secrets are Missing
1. **Check secret names** for typos
2. **Verify repository permissions**
3. **Check environment settings**
4. **Contact repository administrator**

## Contact Information

For issues with secrets configuration:
- **DevOps Team**: devops@valven.com
- **Security Team**: security@valven.com
- **Repository Admin**: admin@valven.com