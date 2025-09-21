# CI/CD Pipeline Documentation

This document describes the Continuous Integration and Continuous Deployment (CI/CD) pipeline for the Valven E-commerce Platform.

## Pipeline Overview

The CI/CD pipeline is designed to ensure code quality, security, and reliable deployments across different environments. It consists of multiple stages that run automatically on code changes and can be triggered manually.

## Pipeline Stages

### 1. Code Quality & Security
- **SonarQube Analysis**: Code quality and security analysis
- **OWASP Dependency Check**: Vulnerability scanning for dependencies
- **SpotBugs Analysis**: Static code analysis for bug detection
- **PMD Analysis**: Code quality and style analysis
- **Code Formatting Check**: Ensures consistent code formatting

### 2. Test Suite
- **Unit Tests**: Fast, isolated tests for individual components
- **Integration Tests**: Tests that verify component interactions
- **Test Coverage**: JaCoCo generates code coverage reports
- **Test Reports**: Detailed test results and failure analysis

### 3. Performance Tests
- **Load Testing**: JMeter-based load tests
- **Stress Testing**: Gatling-based stress tests
- **Performance Metrics**: Response time and throughput analysis

### 4. Build & Push Docker Images
- **Multi-architecture Builds**: Supports both AMD64 and ARM64
- **Container Registry**: Pushes to GitHub Container Registry
- **Image Tagging**: Automatic versioning based on branch and commit

### 5. Security Scan
- **Trivy Vulnerability Scanner**: Container image security scanning
- **SARIF Reports**: Security findings in standardized format
- **GitHub Security Advisories**: Integration with GitHub security features

### 6. Deployment
- **Staging Environment**: Automatic deployment on develop branch
- **Production Environment**: Manual deployment on main branch
- **Health Checks**: Post-deployment verification
- **Rollback Capability**: Quick rollback in case of issues

## Configuration Files

### Workflow Files
- `.github/workflows/ci-cd.yml`: Main CI/CD pipeline
- `.github/workflows/ci-cd-improved.yml`: Enhanced pipeline with additional features
- `.github/workflows/security.yml`: Dedicated security scanning pipeline

### Maven Configuration
- `pom-ci.xml`: CI-specific Maven configuration with all quality plugins
- `spotbugs-include.xml`: SpotBugs inclusion rules
- `spotbugs-exclude.xml`: SpotBugs exclusion rules for false positives
- `owasp-suppressions.xml`: OWASP dependency check suppressions

## Environment Variables

### Required Secrets
- `SONAR_TOKEN`: SonarQube authentication token
- `GITHUB_TOKEN`: GitHub API token (automatically provided)
- `SLACK_WEBHOOK`: Slack webhook for notifications (optional)

### Environment-specific Variables
- `SPRING_DATASOURCE_URL`: Database connection URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `SPRING_DATA_REDIS_HOST`: Redis host
- `JWT_SECRET`: JWT signing secret

## Quality Gates

### Code Quality
- **Coverage Threshold**: Minimum 80% code coverage
- **Duplication**: Maximum 3% code duplication
- **Maintainability**: Maintainability rating A
- **Reliability**: Reliability rating A
- **Security**: Security rating A

### Security
- **Critical Vulnerabilities**: 0 allowed
- **High Vulnerabilities**: 0 allowed
- **Medium Vulnerabilities**: Maximum 5 allowed
- **Dependency Vulnerabilities**: All dependencies must be up to date

### Performance
- **Response Time**: 95th percentile < 2 seconds
- **Throughput**: Minimum 100 requests/second
- **Error Rate**: Maximum 1% error rate

## Manual Triggers

### Workflow Dispatch
The pipeline can be manually triggered with environment selection:

```bash
# Trigger staging deployment
gh workflow run "Enhanced CI/CD Pipeline" -f environment=staging

# Trigger production deployment
gh workflow run "Enhanced CI/CD Pipeline" -f environment=production
```

### Branch Protection Rules
- **Main Branch**: Requires pull request reviews and status checks
- **Develop Branch**: Requires status checks
- **Feature Branches**: No restrictions

## Monitoring and Notifications

### Slack Integration
- **Deployment Notifications**: Success/failure notifications
- **Security Alerts**: Critical security findings
- **Quality Gate Failures**: Code quality issues

### GitHub Integration
- **Status Checks**: Required for merging
- **Security Advisories**: Automatic vulnerability tracking
- **Dependabot**: Automatic dependency updates

## Troubleshooting

### Common Issues

#### Pipeline Failures
1. **Test Failures**: Check test logs and fix failing tests
2. **Quality Gate Failures**: Address code quality issues
3. **Security Vulnerabilities**: Update dependencies or add suppressions
4. **Build Failures**: Check Docker build logs and dependencies

#### Performance Issues
1. **Slow Tests**: Optimize test execution time
2. **Build Timeouts**: Increase timeout values or optimize build process
3. **Resource Limits**: Check GitHub Actions resource limits

### Debug Commands

```bash
# Run quality checks locally
mvn clean verify -Pci

# Run security scan locally
mvn org.owasp:dependency-check-maven:check

# Run code formatting
mvn spotless:apply

# Run static analysis
mvn spotbugs:check pmd:check
```

## Best Practices

### Code Quality
- Write comprehensive unit tests
- Maintain high code coverage
- Follow coding standards
- Use meaningful commit messages

### Security
- Keep dependencies updated
- Review security reports regularly
- Use secure coding practices
- Implement proper input validation

### Performance
- Optimize database queries
- Use caching effectively
- Monitor performance metrics
- Load test before deployment

### Deployment
- Use blue-green deployments
- Implement proper health checks
- Monitor application metrics
- Have rollback procedures ready

## Pipeline Metrics

### Build Time
- **Code Quality**: ~5 minutes
- **Test Suite**: ~10 minutes
- **Performance Tests**: ~15 minutes
- **Build & Push**: ~8 minutes
- **Security Scan**: ~3 minutes
- **Deployment**: ~5 minutes

### Resource Usage
- **CPU**: 2 cores per job
- **Memory**: 4GB per job
- **Storage**: 10GB per job
- **Network**: Standard GitHub Actions bandwidth

## Future Improvements

### Planned Enhancements
- **Parallel Test Execution**: Reduce test execution time
- **Caching Improvements**: Better dependency caching
- **Multi-environment Testing**: Test against multiple environments
- **Automated Rollback**: Automatic rollback on health check failures
- **Performance Baselines**: Track performance regression
- **Security Scanning**: Enhanced security scanning capabilities

### Monitoring Improvements
- **Real-time Metrics**: Live pipeline monitoring
- **Cost Optimization**: Resource usage optimization
- **Alerting**: Proactive failure notifications
- **Dashboards**: Visual pipeline status

## Support

For issues related to the CI/CD pipeline:

1. Check the GitHub Actions logs
2. Review this documentation
3. Contact the DevOps team
4. Create an issue in the repository

## References

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Maven Plugin Documentation](https://maven.apache.org/plugins/)
- [SonarQube Documentation](https://docs.sonarqube.org/)
- [OWASP Dependency Check](https://jeremylong.github.io/DependencyCheck/)
- [SpotBugs Documentation](https://spotbugs.github.io/)
- [PMD Documentation](https://pmd.github.io/)